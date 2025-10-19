@file:OptIn(ExperimentalDecomposeApi::class)

package com.starfruit.navtuto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.LazyChildItems
import com.starfruit.util.decompose.LazyChildItemsPreview
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.lazyitems.ChildItemsLifecycleController
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun ListView(component: ListComponent) {
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

val listComponentPreview = object: ListComponent {
    override val items: LazyChildItems<Item, ItemComponent> = LazyChildItemsPreview(generatePreviewItems())
    override fun goBack() {}
    fun generatePreviewItems(): Map<Item, ItemComponent> {
        return (1..10).associate { index ->
            val item = Item(id = index, data = "Item $index")
            item to ItemComponentPreview(item)
        }
    }
}
