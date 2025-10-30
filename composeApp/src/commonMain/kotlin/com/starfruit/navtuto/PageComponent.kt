package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface IPageComponent {
    val data: String
}

class PageComponent(
    componentContext: ComponentContext,
    override val data: String,
) : ComponentContext by componentContext, IPageComponent {
    class Factory() {
        operator fun invoke(
            componentContext: ComponentContext,
            data: String
        ): PageComponent = PageComponent(
            componentContext = componentContext,
            data = data
        )
    }
}
