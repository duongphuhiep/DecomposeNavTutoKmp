package com.starfruit.template

import com.arkivanov.decompose.ComponentContext

class MyNormalComponent(
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
) : ComponentContext by componentContext {

    fun goBack() = onGoBack()

    class Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onGoBack: () -> Unit,
        ) = MyNormalComponent(
            componentContext = componentContext,
            onGoBack = onGoBack,
        )
    }
}
