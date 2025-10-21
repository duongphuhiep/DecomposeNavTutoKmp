@file:OptIn(ExperimentalDecomposeApi::class)

package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.*


interface ListComponent {
    val items: LazyChildItems<Item, ItemComponent>
    fun goBack()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onGoBack: () -> Unit,
        ): ListComponent
    }
}

class DefaultListComponent private constructor(
    componentContext: ComponentContext,
    private val itemComponentFactory: ItemComponent.Factory,
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
            itemComponentFactory(
                componentContext = childComponentContext,
                item = item,
            )
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

    override fun goBack() = onGoBack()

    class Factory(
        private val itemComponentFactory: ItemComponent.Factory,
    ): ListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onGoBack: () -> Unit,
        ): ListComponent = DefaultListComponent(
            componentContext = componentContext,
            itemComponentFactory = itemComponentFactory,
            onGoBack = onGoBack,
        )
    }
}
