package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable


interface PagesComponent {
    val pages: Value<ChildPages<*, PageComponent>>

    fun selectPage(index: Int)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): PagesComponent
    }
}

class DefaultPagesComponent private constructor(
    componentContext: ComponentContext,
    private val pageComponentFactory: PageComponent.Factory
) : PagesComponent, ComponentContext by componentContext {

    private val navigation = PagesNavigation<Config>()

    override val pages: Value<ChildPages<*, PageComponent>> =
        childPages(
            source = navigation,
            serializer = Config.serializer(), // Or null to disable navigation state saving
            initialPages = {
                Pages(
                    items = List(4) { index -> Config(data = "Item $index") },
                    selectedIndex = 0,
                )
            },
        ) { config, childComponentContext ->
            pageComponentFactory(
                componentContext = childComponentContext,
                data = config.data,
            )
        }

    override fun selectPage(index: Int) {
        navigation.select(index = index)
    }

    @Serializable // kotlinx-serialization plugin must be applied
    private data class Config(val data: String)

    class Factory(
        private val pageComponentFactory: PageComponent.Factory
    ): PagesComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): PagesComponent = DefaultPagesComponent(
            componentContext = componentContext,
            pageComponentFactory = pageComponentFactory,
        )
    }
}
