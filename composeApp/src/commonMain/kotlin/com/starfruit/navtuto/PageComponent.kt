package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

class PageComponent(
    componentContext: ComponentContext,
    val data: String,
) :  ComponentContext by componentContext {

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
