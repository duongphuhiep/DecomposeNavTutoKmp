package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface IMainComponent {
    fun selectItem(itemId: Int)
    fun goBack()
}

class MainComponent(
    componentContext: ComponentContext,
    private val onSelectItem: (i: Int) -> Unit,
    private val onGoBack: () -> Unit
) : ComponentContext by componentContext, IMainComponent {
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

    override fun selectItem(itemId: Int) {
        onSelectItem(itemId)
    }

    override fun goBack() = onGoBack()
}
