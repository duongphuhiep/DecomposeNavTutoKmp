package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface MainComponent {
    fun selectItem(itemId: Int)
    fun goBack()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onSelectItem: (i: Int) -> Unit,
            onGoBack: () -> Unit,
        ): MainComponent
    }
}

class DefaultMainComponent private constructor(
    componentContext: ComponentContext,
    private val onSelectItem: (i: Int) -> Unit,
    private val onGoBack: () -> Unit
) : MainComponent, ComponentContext by componentContext {
    override fun selectItem(itemId: Int) {
        onSelectItem(itemId)
    }

    override fun goBack() = onGoBack()

    class Factory() : MainComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onSelectItem: (i: Int) -> Unit,
            onGoBack: () -> Unit,
        ): MainComponent = DefaultMainComponent(
            componentContext = componentContext,
            onSelectItem = onSelectItem,
            onGoBack = onGoBack,
        )
    }
}
