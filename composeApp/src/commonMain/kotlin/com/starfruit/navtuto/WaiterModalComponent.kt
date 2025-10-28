package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue

class WaiterModalComponent(
    private val componentContext: ComponentContext,
    val text: MutableValue<String>
) : ComponentContext by componentContext {
    class Factory  {
        operator fun invoke(
            componentContext: ComponentContext,
            text: MutableValue<String>
        ) = WaiterModalComponent(componentContext, text)
    }
}

