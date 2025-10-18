@file:OptIn(ExperimentalDecomposeApi::class)

package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.children.transientNavStateSaver
import com.arkivanov.decompose.router.items.Items
import com.arkivanov.decompose.router.items.ItemsNavigation
import com.arkivanov.decompose.router.items.LazyChildItems
import com.arkivanov.decompose.router.items.childItems
import com.arkivanov.decompose.router.items.setItems

interface ListComponent {
    val items: LazyChildItems<Item, ItemComponent>
    fun goBack()
}

class DefaultListComponent(
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
) : ListComponent, ComponentContext by componentContext {
    private val navigation = ItemsNavigation<Item>()

    override val items: LazyChildItems<Item, ItemComponent> =
        childItems(
            source = navigation,

            //survive process dead
            serializer = Item.serializer(), // Or null to disable navigation state saving
            //if the list is big, replace the serializer with the stateSaver
            //stateSaver = transientNavStateSaver(),

            initialItems = {
                Items(
                    items = List(100) { index ->
                        Item(id = index, data = "Item $index")
                    },
                )
            },
        ) { item, childComponentContext ->
            DefaultItemComponent(
                componentContext = childComponentContext,
                item = item,
            )
        }

    override fun goBack() {
        onGoBack()
    }

    // Add items
    fun addMoreItems(newItems: List<Item>) {
        navigation.setItems { currentItems -> currentItems + newItems }
    }

    // Remove items
    fun removeItem(itemToRemove: Item) {
        navigation.setItems { currentItems -> currentItems - itemToRemove }
    }

    // Replace all items
    fun replaceAllItems(newItems: List<Item>) {
        navigation.setItems { newItems }
    }
}