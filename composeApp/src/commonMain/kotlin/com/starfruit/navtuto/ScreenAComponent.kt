package com.starfruit.navtuto

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScreenAView(component: ScreenAComponent) {
    val text by component.text.subscribeAsState()
    val displayDialog by component.dialog.subscribeAsState()
    Column {
        Text("Screen A")
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

        displayDialog.child?.instance?.also {
            AlertDialogView(it)
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 480, heightDp = 800)
fun ScreenAPreview() {
    MaterialTheme {
        ScreenAView(ScreenAComponentPreview)
    }
}

interface ScreenAComponent {
    val dialog: Value<ChildSlot<*, AlertDialogComponent>>
    val text: Value<String>
    fun goToScreenB()
    fun updateText(text: String)
    fun openAlertDialog()
}

class DefaultScreenAComponent(
    componentContext: ComponentContext,
    private val onGoToScreenB: (text: String) -> Unit
) : ScreenAComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<AlertDialogConfig>()
    override val dialog: Value<ChildSlot<*, AlertDialogComponent>> =
        childSlot(
            source = dialogNavigation,
            serializer = AlertDialogConfig.serializer(), // Or null to disable navigation state saving
            handleBackButton = true, // Close the dialog on back button press
        ) { config, childComponentContext ->
            DefaultAlertDialogComponent(
                componentContext = childComponentContext,
                text = config.text,
                onDismissed = ::dialogDismissed,
                onConfirmed = ::dialogConfirmed
            )
        }

    private val _text = MutableValue("")
    override val text: Value<String> = _text
    override fun goToScreenB() {
        onGoToScreenB(text.value)
    }
    override fun updateText(text: String) {
        _text.value = text
    }
    override fun openAlertDialog() {
        dialogNavigation.activate(AlertDialogConfig(text.value))
    }
    fun dialogDismissed() {
        dialogNavigation.dismiss()
        Logger.i("👻 Dismissed")
    }
    fun dialogConfirmed() {
        dialogNavigation.dismiss()
        Logger.i("👻 Confirmed")
    }

    @Serializable
    private data class AlertDialogConfig(val text: String)
}

val ScreenAComponentPreview = object: ScreenAComponent {
    override val dialog: Value<ChildSlot<*, AlertDialogComponent>>
        get() = TODO("Not yet implemented")
    override val text: Value<String> = MutableValue("This is screen A")
    override fun goToScreenB() {}
    override fun updateText(text: String) {}
    override fun openAlertDialog() {}
}
