/*
Decompose + Kodein integration
https://medium.com/@yeldar.nurpeissov/master-kotlin-multiplatform-navigation-with-decompose-add-di-with-kodein-and-koin-405462b2691b
*/
package com.starfruit.template

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

interface DetailComponent {
    val model: Value<Post>
    fun onBackPressed()
    // 1
    fun interface Factory {
        operator fun invoke( // 2
            componentContext: ComponentContext,
            postId: String,
            onFinished: () -> Unit,
        ): DetailComponent
    }
}
class DefaultDetailComponent(
    componentContext: ComponentContext,
    postId: String, // 1
    private val repository: PostRepository, // 2
    private val onFinished: () -> Unit,
) : DetailComponent, ComponentContext by componentContext {

    // 3
    override val model: Value<Post> = MutableValue(repository.getPost(postId))

    override fun onBackPressed() = onFinished()

    // 4
    class Factory(
        private val repository: PostRepository,
    ) : DetailComponent.Factory {

        override fun invoke(
            componentContext: ComponentContext,
            postId: String,
            onFinished: () -> Unit,
        ): DetailComponent = DefaultDetailComponent( // 5
            componentContext = componentContext,
            postId = postId,
            onFinished = onFinished,
            repository = repository,
        )
    }
}

interface ListComponent {
    val model: Value<List<Post>>

    fun onPostClicked(post: Post)

    // 1
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            postClicked: (postId: String) -> Unit,
        ): ListComponent
    }
}
class DefaultListComponent(
    componentContext: ComponentContext,
    repository: PostRepository, // 1
    private val postClicked: (postId: String) -> Unit, // 2
) : ListComponent, ComponentContext by componentContext {

    // 3
    override val model: Value<List<Post>> = MutableValue(repository.getAllPosts())

    override fun onPostClicked(post: Post) = postClicked(post.id)

    // 4
    class Factory(
        private val repository: PostRepository
    ) : ListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            postClicked: (postId: String) -> Unit
        ): ListComponent {
            return DefaultListComponent(
                componentContext = componentContext,
                postClicked = postClicked,
                repository = repository,
            )
        }
    }
}

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class List(val component: ListComponent) : Child
        class Detail(val component: DetailComponent) : Child
    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): RootComponent
    }
}
class DefaultRootComponent( // 1
    componentContext: ComponentContext,
    private val listComponentFactory: ListComponent.Factory,
    private val detailComponentFactory: DetailComponent.Factory,
) : RootComponent, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Config.List,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        Config.List -> RootComponent.Child.List(
            listComponentFactory( // 2
                componentContext = componentContext,
                postClicked = { postId -> nav.pushNew(Config.Detail(postId)) }
            )
        )

        is Config.Detail -> RootComponent.Child.Detail(
            detailComponentFactory( // 3
                componentContext = componentContext,
                postId = config.postId,
                onFinished = { nav.pop() },
            )
        )
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object List : Config

        @Serializable
        data class Detail(val postId: String) : Config // 4
    }

    // 5
    class Factory(
        private val listComponentFactory: ListComponent.Factory,
        private val detailComponentFactory: DetailComponent.Factory,
    ) : RootComponent.Factory {
        override fun invoke(componentContext: ComponentContext): RootComponent {
            return DefaultRootComponent(
                listComponentFactory = listComponentFactory,
                detailComponentFactory = detailComponentFactory,
                componentContext = componentContext,
            )
        }
    }
}

val kodeinDI = DI {
    bindSingleton<PostRepository> { DefaultPostRepository() }

    bindSingleton<DetailComponent.Factory> {
        DefaultDetailComponent.Factory(
            repository = instance(),
        )
    }

    bindSingleton<ListComponent.Factory> {
        DefaultListComponent.Factory(
            repository = instance(),
        )
    }

    bindSingleton<RootComponent.Factory> {
        DefaultRootComponent.Factory(
            detailComponentFactory = instance(),
            listComponentFactory = instance(),
        )
    }
}

data class Post(val id: String, val title: String, val description: String, val author: String)

interface PostRepository {
    fun getAllPosts(): List<Post>
    fun getPost(id: String): Post
}

class DefaultPostRepository : PostRepository {
    private val posts = List(16) {
        Post(
            id = it.toString(),
            title = "Title-#$it",
            description = "Description-#$it",
            author = "Author-#$it",
        )
    }

    override fun getAllPosts(): List<Post> = posts

    override fun getPost(id: String): Post = posts.first { it.id == id }
}

