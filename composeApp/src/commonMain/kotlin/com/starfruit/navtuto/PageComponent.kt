package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface PageComponent {
    val data: String

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            data: String
        ): PageComponent
    }
}

class DefaultPageComponent private constructor(
    componentContext: ComponentContext,
    override val data: String,
) : PageComponent, ComponentContext by componentContext {

    class Factory(): PageComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            data: String
        ): PageComponent = DefaultPageComponent(
            componentContext = componentContext,
            data = data
        )
    }
}
