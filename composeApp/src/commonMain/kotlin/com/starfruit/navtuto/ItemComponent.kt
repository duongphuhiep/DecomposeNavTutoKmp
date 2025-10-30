package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import kotlinx.serialization.Serializable

@Serializable // kotlinx-serialization plugin must be applied
data class Item(val id: Int, val data: String)

interface IItemComponent {
    val item: Item
}

class ItemComponent(
    componentContext: ComponentContext,
    override val item: Item,
) : ComponentContext by componentContext, IItemComponent {
    class Factory() {
        operator fun invoke(
            componentContext: ComponentContext,
            item: Item
        ): ItemComponent = ItemComponent(
            componentContext = componentContext,
            item = item
        )
    }
}
