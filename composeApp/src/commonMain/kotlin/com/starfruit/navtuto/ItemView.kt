package com.starfruit.navtuto

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ItemView(component: IItemComponent) {
    Row {
        Text(component.item.toString())
    }
}

@Composable
@Preview(showBackground = true)
private fun ItemPreview() {
    MaterialTheme {
        ItemView(itemComponentPreview)
    }
}

val itemComponentPreview = object: IItemComponent {
    override val item = Item(1, "huh")
}
