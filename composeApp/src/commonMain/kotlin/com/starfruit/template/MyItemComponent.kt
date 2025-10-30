package com.starfruit.template

import com.arkivanov.decompose.ComponentContext

interface IMyItemComponent {}
class MyItemComponent(
    componentContext: ComponentContext,
    private val item: String,
) : ComponentContext by componentContext, IMyItemComponent {
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
