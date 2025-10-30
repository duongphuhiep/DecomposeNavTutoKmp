package com.starfruit.template

import com.arkivanov.decompose.ComponentContext

interface IMyNormalComponent {
    fun goBack()
}
class MyNormalComponent(
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
) : ComponentContext by componentContext, IMyNormalComponent {
    class Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onGoBack: () -> Unit,
        ) = MyNormalComponent(
            componentContext = componentContext,
            onGoBack = onGoBack,
        )
    }

    override fun goBack() = onGoBack()
}
