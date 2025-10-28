package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

class AlertDialogComponent(
    private val componentContext: ComponentContext,
    val text: String,
    private val onDismissed: () -> Unit,
    private val onConfirmed: () -> Unit,
): ComponentContext by componentContext {
    fun dismiss() = onDismissed()
    fun confirm() = onConfirmed()

    class Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            text: String,
            onDismissed: () -> Unit,
            onConfirmed: () -> Unit
        ) = AlertDialogComponent(componentContext, text, onDismissed, onConfirmed)
    }
}

