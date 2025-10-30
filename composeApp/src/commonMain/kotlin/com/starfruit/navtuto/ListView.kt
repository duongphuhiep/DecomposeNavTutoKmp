@file:OptIn(ExperimentalDecomposeApi::class)

package com.starfruit.navtuto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lazyitems.ChildItemsLifecycleController
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.items.LazyChildItems
import com.starfruit.util.decompose.LazyChildItemsPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ListView(component: IListComponent) {
    Column(modifier = Modifier.fillMaxSize()) {
        val childItems by component.items.subscribeAsState()
        val lazyListState = rememberLazyListState()
        ChildItemsLifecycleController(
            items = component.items,
            lazyListState = lazyListState,
            forwardPreloadCount = 3, // Preload 3 components forward
            backwardPreloadCount = 3, // Preload 3 components backward
            itemIndexConverter = { it },
        )
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .weight(1f) // take all remaining space
            ) {
            items(items = childItems.items) { config ->
                ItemView(component = component.items[config])
            }
        }
        Button(onClick = {component.goBack()}) {
            Text("Go back")
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ListPreview() {
    MaterialTheme {
        ListView(listComponentPreview)
    }
}

//val listComponentPreview = ListComponent(
//    componentContext = componentContextPreview,
//    itemComponentFactory = ItemComponent.Factory(),
//    onGoBack = {}
//)

val listComponentPreview = object: IListComponent {
    override val items: LazyChildItems<Item, IItemComponent> = LazyChildItemsPreview(generatePreviewItems())
    override fun goBack() {}
    fun generatePreviewItems(): Map<Item, IItemComponent> {
        return (1..10).associate { index ->
            val item = Item(id = index, data = "Item $index")
            item to itemComponentPreview
        }
    }
}
