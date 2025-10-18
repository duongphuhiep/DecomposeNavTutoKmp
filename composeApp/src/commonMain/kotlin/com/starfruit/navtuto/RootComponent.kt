package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

interface RootComponent {
    val childStack: Value<ChildStack<Configuration, Child>>

    sealed class Child {
        data class ScreenA(val component: ScreenAComponent) : Child()
        data class ScreenB(val component: ScreenBComponent) : Child()
        data class Pages(val component: PagesComponent) : Child()
        data class Panels(val component: PanelsComponent) : Child()
        data class List(val component: ListComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object ScreenA : Configuration()
        @Serializable
        data class ScreenB(val text: String) : Configuration()
        @Serializable
        data object Pages : Configuration()
        @Serializable
        data object Panels : Configuration()
        @Serializable
        data object List : Configuration()
    }
}

class DefaultRootComponent(componentContext: ComponentContext) : RootComponent,
    ComponentContext by componentContext {
    private val navigation = StackNavigation<RootComponent.Configuration>()
    override val childStack = childStack(
        source = navigation,
        serializer = RootComponent.Configuration.serializer(),
        initialConfiguration = RootComponent.Configuration.ScreenA,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: RootComponent.Configuration,
        context: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is RootComponent.Configuration.ScreenA ->
                RootComponent.Child.ScreenA(
                    DefaultScreenAComponent(
                        componentContext = context,
                        onGoToScreenB = {
                            navigation.pushNew(RootComponent.Configuration.ScreenB(it))
                        },
                        onGoToPages = {
                            navigation.pushNew(RootComponent.Configuration.Pages)
                        },
                        onGoToPanels = {
                            navigation.pushNew(RootComponent.Configuration.Panels)
                        },
                        onGoToList = {
                            navigation.pushNew(RootComponent.Configuration.List)
                        }
                    )
                )
            is RootComponent.Configuration.ScreenB ->
                RootComponent.Child.ScreenB(
                    DefaultScreenBComponent(
                        componentContext = context,
                        text = config.text,
                        onGoBack = {
                            navigation.pop()
                        },
                    ))
            is RootComponent.Configuration.Pages ->
                RootComponent.Child.Pages(
                    DefaultPagesComponent(context)
                )
            is RootComponent.Configuration.Panels ->
                RootComponent.Child.Panels(
                    DefaultPanelsComponent(context, onGoBack = {
                        navigation.pop()
                    })
                )
            is RootComponent.Configuration.List ->
                RootComponent.Child.List(
                    DefaultListComponent(context, onGoBack = {
                        navigation.pop()
                    })
                )
        }
    }
}