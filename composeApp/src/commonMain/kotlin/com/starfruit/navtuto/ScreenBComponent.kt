package com.starfruit.navtuto

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
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
fun ScreenBPreview() {
    val component = object : ScreenBComponent {
        override val text = "preview content"
        override fun GoBack() {}
    }
    MaterialTheme {
        ScreenBView(component)
    }
}

interface ScreenBComponent {
    val text: String
    fun GoBack();
}

class DefaultScreenBComponent(
    componentContext: ComponentContext,
    override val text: String,
    private val navigateBack: () -> Unit
) : ScreenBComponent, ComponentContext by componentContext {
    override fun GoBack() {
        navigateBack()
    }
}