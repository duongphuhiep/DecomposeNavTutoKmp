package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext


interface IAlertDialogComponent {
    val text: String
    fun dismiss()
    fun confirm()
}

class AlertDialogComponent(
    private val componentContext: ComponentContext,
    override val text: String,
    private val onDismissed: () -> Unit,
    private val onConfirmed: () -> Unit,
): ComponentContext by componentContext, IAlertDialogComponent {
    class Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            text: String,
            onDismissed: () -> Unit,
            onConfirmed: () -> Unit
        ) = AlertDialogComponent(componentContext, text, onDismissed, onConfirmed)
    }

    override fun dismiss() = onDismissed()
    override fun confirm() = onConfirmed()
}

