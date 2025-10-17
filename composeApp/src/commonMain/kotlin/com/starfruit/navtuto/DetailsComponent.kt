package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface DetailsComponent {
    val itemId: Int
    fun goBack()
}

class DefaultDetailsComponent(
    componentContext: ComponentContext,
    override val itemId: Int,
    private val onGoBack: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {
    override fun goBack() {
        onGoBack()
    }
}