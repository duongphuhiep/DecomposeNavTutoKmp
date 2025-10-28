package com.starfruit.navtuto

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.starfruit.util.Optional
import kotlinx.serialization.Serializable

class ScreenAComponent(
    componentContext: ComponentContext,
    private val alertDialogComponentFactory: AlertDialogComponent.Factory,
    private val onGoToScreenB: (text: String) -> Unit,
    private val onGoToPages: () -> Unit,
    private val onGoToPanels: () -> Unit,
    private val onGoToList: () -> Unit
) : ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<AlertDialogConfig>()
    val alertDialogChildSlot: Value<ChildSlot<*, AlertDialogComponent>> =
        childSlot(
            source = dialogNavigation,
            serializer = AlertDialogConfig.serializer(), // Or null to disable navigation state saving
            handleBackButton = true, // Close the dialog on back button press
        ) { config, childComponentContext ->
            alertDialogComponentFactory(
                componentContext = childComponentContext,
                text = config.text,
                onDismissed = ::dialogDismissed,
                onConfirmed = ::dialogConfirmed
            )
        }
    val alertDialog =
        alertDialogChildSlot.map { Optional(it.child?.instance) }

    private val _dialogIsConfirmed = MutableValue(Optional<Boolean>(null))
    val dialogIsConfirmed: Value<Optional<Boolean>> = _dialogIsConfirmed

    private val _text = MutableValue("")
    val text: Value<String> = _text

    fun goToScreenB() {
        onGoToScreenB(text.value)
    }

    fun updateText(text: String) {
        _text.value = text
    }

    fun openAlertDialog() {
        dialogNavigation.activate(AlertDialogConfig(text.value))
    }

    fun resetDialogResult() {
        _dialogIsConfirmed.value = Optional(null)
    }

    fun goToPages() {
        onGoToPages()
    }

    fun goToPanels() {
        onGoToPanels()
    }

    fun goToList() {
        onGoToList()
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

    class Factory(
        private val alertDialogComponentFactory: AlertDialogComponent.Factory,
    ) {
        operator fun invoke(
            componentContext: ComponentContext,
            onGoToScreenB: (text: String) -> Unit,
            onGoToPages: () -> Unit,
            onGoToPanels: () -> Unit,
            onGoToList: () -> Unit
        ): ScreenAComponent = ScreenAComponent(
            componentContext = componentContext,
            alertDialogComponentFactory = alertDialogComponentFactory,
            onGoToScreenB = onGoToScreenB,
            onGoToPages = onGoToPages,
            onGoToPanels = onGoToPanels,
            onGoToList = onGoToList
        )
    }
}
