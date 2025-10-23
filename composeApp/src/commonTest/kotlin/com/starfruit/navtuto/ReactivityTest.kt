package com.starfruit.navtuto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
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

//    @Composable
//    fun Bidon() {
//        val state = MutableStateFlow<Int>(1)
//        val scope = rememberCoroutineScope()
//        val x = state.collectAsState(scope)
//    }
//
//    @Test
//    fun tryStateFlow2() = runTest {
//        val scope = rememberCoroutineScope()
//        val state = MutableStateFlow<Int>(1)
//        val x = state.collectAsState(it)
//
//        withContext(Dispatchers.Default.limitedParallelism(2)) {
//
//            val collectingJob = withTimeout(3000) {
//                launch { //fire and forget
//                    state.collect {
//                        println("🔔 value is changed $it")
//                    }
//                }
//            }
//
//            val emittingJob = launch {
//                for (i in 10..20) {
//                    state.emit(i)
//                    delay(300)
//                }
//            }
//
//            emittingJob.join()
//            collectingJob.join()
//        }
//    }
//
    @Test
    fun tryStateFlow() = runTest {
        val state = MutableStateFlow(1)
        withContext(Dispatchers.Default.limitedParallelism(2)) {

            try {
                val collectingJob = withTimeout(3000) {
                    launch { //fire and forget
                        state.collect {
                            println("🔔 value is changed $it")
                        }
                    }
                }
            }
            catch (ex: TimeoutCancellationException) {}

        }
        async {
            for (i in 10..20) {
                state.emit(i)
                delay(300)
            }
        }.await()
    }

}
