package com.starfruit.navtuto

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.starfruit.util.Optional
import kotlinx.serialization.Serializable

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
    fun goToPages()
}

class DefaultScreenAComponent(
    componentContext: ComponentContext,
    private val onGoToScreenB: (text: String) -> Unit,
    private val onGoToPages: ()->Unit
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

    override fun goToPages() {
        onGoToPages()
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

