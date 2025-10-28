package com.starfruit.template

import com.arkivanov.decompose.ComponentContext

interface NormalComponent {

    fun goBack()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onGoBack: () -> Unit,
        ): NormalComponent
    }
}

class DefaultNormalComponent private constructor(
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
) : NormalComponent, ComponentContext by componentContext {

    override fun goBack() = onGoBack()

    class Factory(): NormalComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onGoBack: () -> Unit,
        ): NormalComponent = DefaultNormalComponent(
            componentContext = componentContext,
            onGoBack = onGoBack,
        )
    }
}
