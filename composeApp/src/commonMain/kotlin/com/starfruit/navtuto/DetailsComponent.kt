package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface IDetailsComponent {
    val itemId: Int
    fun goBack()
}

class DetailsComponent(
    componentContext: ComponentContext,
    override val itemId: Int,
    private val onGoBack: () -> Unit,
) : ComponentContext by componentContext, IDetailsComponent {
    class Factory() {
        operator fun invoke(
            componentContext: ComponentContext,
            itemId: Int,
            onGoBack: () -> Unit,
        ): DetailsComponent = DetailsComponent(
            componentContext = componentContext,
            itemId = itemId,
            onGoBack = onGoBack,
        )
    }

    override fun goBack() = onGoBack()
}
