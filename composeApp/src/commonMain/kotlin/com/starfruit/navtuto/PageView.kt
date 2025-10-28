package com.starfruit.navtuto

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PageView(component: PageComponent) {
    Text("Page: ${component.data}")
}

@Composable
@Preview(showBackground = true)
private fun PagePreview() {
    MaterialTheme {
        PageView(pageComponentPreview)
    }
}

val pageComponentPreview = PageComponent(
    componentContext = componentContextPreview,
    data = "this is a preview page"
)