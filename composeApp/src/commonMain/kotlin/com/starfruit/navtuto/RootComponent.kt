package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext,
    private val screenAComponentFactory: ScreenAComponent.Factory,
    private val screenBComponentFactory: ScreenBComponent.Factory,
    private val pagesComponentFactory: PagesComponent.Factory,
    private val panelsComponentFactory: PanelsComponent.Factory,
    private val listComponentFactory: ListComponent.Factory,
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()
    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.ScreenA,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when (config) {
            is Configuration.ScreenA ->
                Child.ScreenA(
                    screenAComponentFactory(
                        componentContext = context,
                        onGoToScreenB = {
                            navigation.pushNew(Configuration.ScreenB(it))
                        },
                        onGoToPages = {
                            navigation.pushNew(Configuration.Pages)
                        },
                        onGoToPanels = {
                            navigation.pushNew(Configuration.Panels)
                        },
                        onGoToList = {
                            navigation.pushNew(Configuration.List)
                        }
                    )
                )
            is Configuration.ScreenB ->
                Child.ScreenB(
                    screenBComponentFactory(
                        componentContext = context,
                        text = config.text,
                        onGoBack = navigation::pop,
                    ))
            is Configuration.Pages ->
                Child.Pages(
                    pagesComponentFactory(context)
                )
            is Configuration.Panels ->
                Child.Panels(
                    panelsComponentFactory(context, onGoBack = navigation::pop)
                )
            is Configuration.List ->
                Child.List(
                    listComponentFactory(context, onGoBack = navigation::pop)
                )
        }
    }

    sealed interface Child {
        data class ScreenA(val component: ScreenAComponent) : Child
        data class ScreenB(val component: ScreenBComponent) : Child
        data class Pages(val component: PagesComponent) : Child
        data class Panels(val component: PanelsComponent) : Child
        data class List(val component: ListComponent) : Child
    }

    @Serializable
    private sealed interface Configuration {
        @Serializable
        data object ScreenA : Configuration
        @Serializable
        data class ScreenB(val text: String) : Configuration
        @Serializable
        data object Pages : Configuration
        @Serializable
        data object Panels : Configuration
        @Serializable
        data object List : Configuration
    }

    class Factory(
        private val screenAComponentFactory: ScreenAComponent.Factory,
        private val screenBComponentFactory: ScreenBComponent.Factory,
        private val pagesComponentFactory: PagesComponent.Factory,
        private val panelsComponentFactory: PanelsComponent.Factory,
        private val listComponentFactory: ListComponent.Factory,
    ) {
        operator fun invoke(
            componentContext: ComponentContext,
        ): RootComponent = RootComponent(
            componentContext = componentContext,
            screenAComponentFactory = screenAComponentFactory,
            screenBComponentFactory = screenBComponentFactory,
            pagesComponentFactory = pagesComponentFactory,
            panelsComponentFactory = panelsComponentFactory,
            listComponentFactory = listComponentFactory,
        )
    }
}
