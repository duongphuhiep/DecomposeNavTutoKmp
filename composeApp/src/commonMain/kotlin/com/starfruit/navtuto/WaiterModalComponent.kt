package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue

interface WaiterModalComponent {
    val text: MutableValue<String>

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            text: MutableValue<String>
        ): WaiterModalComponent
    }
}

class DefaultWaiterModalComponent private constructor(
    private val componentContext: ComponentContext,
    override val text: MutableValue<String>
) : WaiterModalComponent, ComponentContext by componentContext {

    class Factory : WaiterModalComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            text: MutableValue<String>
        ) = DefaultWaiterModalComponent(componentContext, text)
    }
}

