package com.starfruit.navtuto

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CannabisSolid
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun WaiterModalView(component: WaiterModalComponent) {
    AlertDialog(
        icon = {
            Icon(
                LineAwesomeIcons.CannabisSolid,
                contentDescription = ""
            )
        },
        title = {
            Text(text = "Waiting for something")
        },
        text = {
            val waiterText by component.text.subscribeAsState()
            Text(text = waiterText)
        },
        onDismissRequest = {},
        confirmButton = {},
    )
}

@Composable
@Preview
private fun WaiterModalPreview() {
    MaterialTheme {
        WaiterModalView(waiterModalComponentPreview)
    }
}

val waiterModalComponentPreview = WaiterModalComponent(
    componentContext = componentContextPreview,
    text = MutableValue("haha")
)