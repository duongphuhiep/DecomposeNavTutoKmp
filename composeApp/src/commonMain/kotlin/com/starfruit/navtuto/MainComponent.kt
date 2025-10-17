package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface MainComponent {
    fun selectItem(itemId: Int)
    fun goBack()
}

class DefaultMainComponent(
    componentContext: ComponentContext,
    private val onSelectItem: (i: Int) -> Unit,
    private val onGoBack: () -> Unit
) : MainComponent, ComponentContext by componentContext {
    override fun selectItem(itemId: Int) {
        onSelectItem(itemId)
    }
    override fun goBack() {
        onGoBack()
    }
}