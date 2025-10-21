package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface AlertDialogComponent {
    val text: String
    fun dismiss()
    fun confirm()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            text: String,
            onDismissed: () -> Unit,
            onConfirmed: () -> Unit
        ): AlertDialogComponent
    }
}

class DefaultAlertDialogComponent private constructor(
    private val componentContext: ComponentContext,
    override val text: String,
    private val onDismissed: () -> Unit,
    private val onConfirmed: () -> Unit,
) : AlertDialogComponent, ComponentContext by componentContext {

    override fun dismiss() = onDismissed()
    override fun confirm() = onConfirmed()

    class Factory : AlertDialogComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            text: String,
            onDismissed: () -> Unit,
            onConfirmed: () -> Unit
        ) = DefaultAlertDialogComponent(componentContext, text, onDismissed, onConfirmed)
    }
}

