package com.starfruit.navtuto

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScreenBView(component: ScreenBComponent) {
    Column {
        Text("Screen B")
        Text(component.text)
        Button({ component.GoBack() }) {
            Text("Go back")
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 480, heightDp = 800)
private fun ScreenBPreview() {
    MaterialTheme {
        ScreenBView(screenBComponentPreview)
    }
}

val screenBComponentPreview = object : ScreenBComponent {
    override val text = "preview content"
    override fun GoBack() {}
}