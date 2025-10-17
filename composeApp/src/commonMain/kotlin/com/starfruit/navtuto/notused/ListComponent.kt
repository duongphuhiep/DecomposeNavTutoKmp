package com.starfruit.navtuto.notused

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface ListComponent {
    val model: Value<State>

    fun onItemSelected(item: String)

    data class State(
        val items: List<String>,
    )
}

class DefaultListComponent(
    componentContext: ComponentContext,
    private val onItemSelectedHandler: (item: String) -> Unit,
) : ListComponent {
    override val model: Value<ListComponent.State> =
        MutableValue(ListComponent.State(items = List(100) { "Item $it" }))

    override fun onItemSelected(item: String) {
        onItemSelectedHandler(item)
    }
}