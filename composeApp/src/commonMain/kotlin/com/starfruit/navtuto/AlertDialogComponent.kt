package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface AlertDialogComponent {
    val text: String
    fun dismiss()
    fun confirm()
}

class DefaultAlertDialogComponent(
    private val componentContext: ComponentContext,
    override val text: String,
    private val onDismissed: () -> Unit,
    private val onConfirmed: () -> Unit,
) : AlertDialogComponent, ComponentContext by componentContext {

    override fun dismiss() {
        onDismissed()
    }

    override fun confirm() {
        onConfirmed()
    }
}

