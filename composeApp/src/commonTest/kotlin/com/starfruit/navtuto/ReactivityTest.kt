package com.starfruit.navtuto

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import kotlin.test.Test
import kotlin.test.assertEquals

class ReactivityTest {
    @Test
    fun `reactive value`() {
        val reactiveValue = MutableValue("hello")
        reactiveValue.subscribe { println("🔔 value is changed $it") }
        reactiveValue.value = "world"
        assertEquals("world", reactiveValue.value)
    }

    @Test
    fun `reactive state class`() {
        data class State(var items: MutableList<String>)

        val reactiveValue = MutableValue(State(MutableList(10) { "Item $it" }))
        reactiveValue.subscribe { println("🔔 value is changed ${it.items}") }

        assertEquals("Item 3", reactiveValue.value.items[3])

        println("✏ mutating item 3 of the list")
        reactiveValue.value.items[3] = "Hello"

        println("✏ mutating the entire list")
        reactiveValue.value.items = mutableListOf<String>("ItemBis0", "ItemBis1")

        println("✏ mutating the entire state object")
        reactiveValue.value = State(mutableListOf<String>("ItemBisBis0", "ItemBisBis1"))
    }

    @Test
    fun `derived state`() {
        data class State(val count: Int)

        // Original state with a counter
        val state = MutableValue(State(count = 0))

        // Derived state
        val isEven: Value<Boolean> = state.map { state ->
            state.count % 2 == 0
        }

        isEven.subscribe { println("🔔 isEven's value is changed ${it}") }

        for (i in listOf(1, 1, 2, 2, 3, 3)) {
            //mutate the state
            state.value = State(i)
        }
    }
}
