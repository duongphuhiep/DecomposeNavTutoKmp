package com.starfruit.template

import com.arkivanov.decompose.ComponentContext

class MyItemComponent(
    componentContext: ComponentContext,
    private val item: String,
) : ComponentContext by componentContext {
    class Factory() {
        operator fun invoke(
            componentContext: ComponentContext,
            item: String
        ) = MyItemComponent(
            componentContext = componentContext,
            item = item
        )
    }
}
