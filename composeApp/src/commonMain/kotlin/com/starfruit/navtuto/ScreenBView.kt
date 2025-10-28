package com.starfruit.navtuto

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.starfruit.util.Optional
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScreenBView(component: ScreenBComponent) {
    Column {
        Text("Screen B")
        Text(component.text)

        val scope = rememberCoroutineScope()
        Button({ component.loadSomeData(scope) }) {
            val loadingState by component.loadingState.subscribeAsState()
            Text("Load some data ($loadingState)")
        }

        val someData by component.someData.subscribeAsState()
        someData.value?.let {
            Text(it)
        }

        Button({ component.showProgression() }) {
            Text("Show Progression")
        }

        Button({ component.goBack() }) {
            Text("Go back")
        }
    }
    val waiterState = component.waiterModalComponent.text.subscribeAsState()
    if (waiterState.value.isNotEmpty()) {
        WaiterModalView(component.waiterModalComponent)
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
    override val someData: Value<Optional<String>> = MutableValue(Optional("some result"))
    override val loadingState: Value<String> = MutableValue("loaded")

    override fun loadSomeData(scope: CoroutineScope) {
        TODO("Not yet implemented")
    }

    override fun showProgression() {
        TODO("Not yet implemented")
    }

    override fun goBack() {}

    override val waiterModalComponent: WaiterModalComponent
        get() = waiterModalComponentPreview
}