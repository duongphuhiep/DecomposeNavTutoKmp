package com.starfruit.navtuto

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.starfruit.navtuto.AlertDialogComponent.Factory
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CannabisSolid
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AlertDialogView(component: AlertDialogComponent) {
    AlertDialog(
        icon = {
            Icon(
                LineAwesomeIcons.CannabisSolid,
                contentDescription = ""
            )
        },
        title = {
            Text(text = "Sample Alert Dialog")
        },
        text = {
            Text(text = "You typed '${component.text}'")
        },
        // when user click outside
        onDismissRequest = component::dismiss,
        confirmButton = {
            TextButton(
                onClick = component::confirm
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = component::dismiss,
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
@Preview
private fun AlertDialogPreview() {
    MaterialTheme {
        AlertDialogView(alertDialogComponentPreview)
    }
}

val alertDialogComponentPreview = Factory()(
    componentContext = componentContextPreview,
    text = "haha",
    onDismissed = {},
    onConfirmed = {}
)