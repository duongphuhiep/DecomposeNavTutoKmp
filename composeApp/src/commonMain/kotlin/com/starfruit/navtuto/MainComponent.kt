package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

class MainComponent(
    componentContext: ComponentContext,
    private val onSelectItem: (i: Int) -> Unit,
    private val onGoBack: () -> Unit
) : ComponentContext by componentContext {
    fun selectItem(itemId: Int) {
        onSelectItem(itemId)
    }

    fun goBack() = onGoBack()

    class Factory() {
        operator fun invoke(
            componentContext: ComponentContext,
            onSelectItem: (i: Int) -> Unit,
            onGoBack: () -> Unit,
        ): MainComponent = MainComponent(
            componentContext = componentContext,
            onSelectItem = onSelectItem,
            onGoBack = onGoBack,
        )
    }
}
