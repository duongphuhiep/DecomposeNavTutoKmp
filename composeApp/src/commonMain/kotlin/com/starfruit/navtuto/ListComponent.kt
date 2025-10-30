@file:OptIn(ExperimentalDecomposeApi::class)

package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.*

interface IListComponent {
    val items: LazyChildItems<Item, IItemComponent>
    fun goBack()
}

class ListComponent(
    componentContext: ComponentContext,
    private val itemComponentFactory: ItemComponent.Factory,
    private val onGoBack: () -> Unit,
) : ComponentContext by componentContext, IListComponent {
    class Factory(
        private val itemComponentFactory: ItemComponent.Factory,
    ) {
        operator fun invoke(
            componentContext: ComponentContext,
            onGoBack: () -> Unit,
        ): ListComponent = ListComponent(
            componentContext = componentContext,
            itemComponentFactory = itemComponentFactory,
            onGoBack = onGoBack,
        )
    }

    private val navigation = ItemsNavigation<Item>()

     override val items: LazyChildItems<Item, IItemComponent> =
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
}
