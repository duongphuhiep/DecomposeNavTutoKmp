package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface ScreenBComponent {
    val text: String
    fun GoBack();
}

class DefaultScreenBComponent(
    componentContext: ComponentContext,
    override val text: String,
    private val navigateBack: () -> Unit
) : ScreenBComponent, ComponentContext by componentContext {
    override fun GoBack() {
        navigateBack()
    }
}

