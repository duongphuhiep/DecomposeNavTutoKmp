package com.starfruit.navtuto

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScreenAView(component: ScreenAComponent) {
    val text by component.text.subscribeAsState()
    Column {
        Text("Screen A")
        OutlinedTextField(text, {
            component.UpdateText(it)
        })
        Button({ component.GoToScreenB() }) {
            Text("Go to ScreenB")
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 480, heightDp = 800)
fun ScreenAPreview() {
    //val context = DefaultComponentContext(LifecycleRegistry())
    val component = object : ScreenAComponent {
        override val text: Value<String> = MutableValue("hey")
        override fun GoToScreenB() {}
        override fun UpdateText(text: String) {}
    }
    MaterialTheme {
        ScreenAView(component)
    }
}

interface ScreenAComponent {
    val text: Value<String>
    fun GoToScreenB()
    fun UpdateText(text: String)
}

class DefaultScreenAComponent(
    componentContext: ComponentContext,
    private val navigateToScreenB: (text: String) -> Unit
) : ScreenAComponent, ComponentContext by componentContext {
    private val _text = MutableValue("")
    override val text: Value<String> = _text

    override fun GoToScreenB() {
        navigateToScreenB(text.value)
    }

    override fun UpdateText(text: String) {
        _text.value = text
    }
}