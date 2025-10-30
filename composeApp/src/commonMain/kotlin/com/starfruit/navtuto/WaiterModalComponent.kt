package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface IWaiterModalComponent {
    val text: MutableValue<String>
}

class WaiterModalComponent(
    private val componentContext: ComponentContext,
    override val text: MutableValue<String>
) : ComponentContext by componentContext, IWaiterModalComponent {
    class Factory  {
        operator fun invoke(
            componentContext: ComponentContext,
            text: MutableValue<String>
        ) = WaiterModalComponent(componentContext, text)
    }
}

