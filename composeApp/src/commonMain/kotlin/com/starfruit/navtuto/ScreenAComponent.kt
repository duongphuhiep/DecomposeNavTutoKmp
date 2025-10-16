package com.starfruit.navtuto

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.starfruit.util.Optional
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
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
        dialogIsConfirmed.value?.also {confirmed ->
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
fun ScreenAPreview() {
    MaterialTheme {
        ScreenAView(ScreenAComponentPreview)
    }
}

interface ScreenAComponent {
    val alertDialog: Value<Optional<AlertDialogComponent>>
    val text: Value<String>
    /**
     * null if the dialog is not show up
     * true if the dialog is confirmed
     * false if the dialog is dismissed
     */
    val dialogIsConfirmed: Value<Optional<Boolean>>
    fun goToScreenB()
    fun updateText(text: String)
    fun openAlertDialog()
    fun resetDialogResult()
}

class DefaultScreenAComponent(
    componentContext: ComponentContext,
    private val onGoToScreenB: (text: String) -> Unit,
) : ScreenAComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<AlertDialogConfig>()
    val alertDialogChildSlot: Value<ChildSlot<*, AlertDialogComponent>> =
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
    override val alertDialog =
        alertDialogChildSlot.map { Optional(it.child?.instance) }

    private val _dialogIsConfirmed = MutableValue(Optional<Boolean>(null))
    override val dialogIsConfirmed: Value<Optional<Boolean>> = _dialogIsConfirmed

    val _text = MutableValue("")
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

    override fun resetDialogResult() {
        _dialogIsConfirmed.value = Optional(null)
    }

    fun dialogDismissed() {
        _dialogIsConfirmed.value = Optional(false)
        dialogNavigation.dismiss()
        //_dialogIsConfirmed.value =  Optional(null)
        Logger.i("👻 Dismissed")
    }

    fun dialogConfirmed() {
        _dialogIsConfirmed.value = Optional(true)
        dialogNavigation.dismiss()
        //_dialogIsConfirmed.value =  Optional(null)
        Logger.i("👻 Confirmed")
    }

    @Serializable
    private data class AlertDialogConfig(val text: String)
}

val ScreenAComponentPreview = object : ScreenAComponent {
    override val alertDialog: Value<Optional<AlertDialogComponent>>
        get() = TODO("Not yet implemented")
    override val text: Value<String> = MutableValue("This is screen A")
    override val dialogIsConfirmed: Value<Optional<Boolean>> = MutableValue(Optional(null))

    override fun goToScreenB() {}
    override fun updateText(text: String) {}
    override fun openAlertDialog() {}
    override fun resetDialogResult() {}
}
