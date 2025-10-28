package com.starfruit.template

import com.arkivanov.decompose.ComponentContext

interface ItemComponent {
    val item: String

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            item: String
        ): ItemComponent
    }
}

class DefaultItemComponent private constructor(
    componentContext: ComponentContext,
    override val item: String,
) : ItemComponent, ComponentContext by componentContext {
    class Factory(): ItemComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            item: String
        ): ItemComponent = DefaultItemComponent(
            componentContext = componentContext,
            item = item
        )
    }
}
