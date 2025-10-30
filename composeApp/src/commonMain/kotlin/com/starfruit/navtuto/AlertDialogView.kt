package com.starfruit.navtuto

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CannabisSolid
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AlertDialogView(component: IAlertDialogComponent) {
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

val alertDialogComponentPreview = object: IAlertDialogComponent {
    override val text = "something to preview"
    override fun dismiss() {}
    override fun confirm() {}
}