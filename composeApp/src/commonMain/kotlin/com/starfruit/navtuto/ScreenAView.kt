package com.starfruit.navtuto

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.starfruit.util.Optional
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScreenAView(component: ScreenAComponent) {
    Column {
        Text("Screen A")

        val text by component.text.subscribeAsState()
        OutlinedTextField(text, {
            component.updateText(it)
        })
        Button({ component.goToScreenB() }) {
            Text("Go to ScreenB")
        }
        //component.dialog.value.child?.instance
        Button({ component.openAlertDialog() }) {
            Text("Open Alert Dialog")
        }

        val displayDialog by component.alertDialog.subscribeAsState()
        displayDialog.value?.also {
            AlertDialogView(it)
        }

        val dialogIsConfirmed by component.dialogIsConfirmed.subscribeAsState()
        dialogIsConfirmed.value?.also { confirmed ->
            val scope = rememberCoroutineScope()
            val snackbarHostState = LocalSnackbarHostState.current
            scope.launch {
                snackbarHostState.showSnackbar(if (confirmed) "Confirmed" else "Dismissed")
                component.resetDialogResult()
            }
            Text(if (confirmed) "Confirmed" else "Dismissed")
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 480, heightDp = 800)
private fun ScreenAPreview() {
    MaterialTheme {
        ScreenAView(screenAComponentPreview)
    }
}

private val screenAComponentPreview = object : ScreenAComponent {
    override val alertDialog: Value<Optional<AlertDialogComponent>>
        get() = TODO("Not yet implemented")
    override val text: Value<String> = MutableValue("This is screen A")
    override val dialogIsConfirmed: Value<Optional<Boolean>> = MutableValue(Optional(null))

    override fun goToScreenB() {}
    override fun updateText(text: String) {}
    override fun openAlertDialog() {}
    override fun resetDialogResult() {}
}