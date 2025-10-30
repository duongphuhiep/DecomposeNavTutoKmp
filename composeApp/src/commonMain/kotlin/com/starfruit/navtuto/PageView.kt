package com.starfruit.navtuto

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PageView(component: IPageComponent) {
    Text("Page: ${component.data}")
}

@Composable
@Preview(showBackground = true)
private fun PagePreview() {
    MaterialTheme {
        PageView(pageComponentPreview)
    }
}

val pageComponentPreview = object : IPageComponent {
    override val data = "This is a preview page"
}