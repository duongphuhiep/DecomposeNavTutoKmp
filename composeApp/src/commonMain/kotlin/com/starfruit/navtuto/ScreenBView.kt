package com.starfruit.navtuto

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.starfruit.util.Optional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

val screenBComponentPreview = ScreenBComponent(
    componentContext = componentContextPreview,
    text = "ha",
    onGoBack = {},
    waiterModalComponentFactory = WaiterModalComponent.Factory(),
    globalCoroutineContext = Dispatchers.Default
)
