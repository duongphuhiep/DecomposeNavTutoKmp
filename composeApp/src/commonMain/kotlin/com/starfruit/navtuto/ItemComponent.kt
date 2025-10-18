package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import kotlinx.serialization.Serializable

interface ItemComponent {
    val item: Item
}

@Serializable // kotlinx-serialization plugin must be applied
data class Item(val id: Int, val data: String)

class DefaultItemComponent(
    componentContext: ComponentContext,
    override val item: Item,
) : ItemComponent, ComponentContext by componentContext