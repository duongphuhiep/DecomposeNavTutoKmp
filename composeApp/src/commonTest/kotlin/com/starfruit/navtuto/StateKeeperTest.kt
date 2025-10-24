package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.testutils.consume
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.coroutines.CoroutineContext
import kotlin.test.*

class StateKeeperTest {

    @Serializable // Comes from kotlinx-serialization
    class State(var someValue: Int = 0)

    /**
     * Make State survive configuration changes (but not process death)
     */
    class RetainedState(savedState: State?) : InstanceKeeper.Instance {
        var state: State = savedState ?: State()
            private set
    }

    @Test
    fun `try StateKeeper api`() {
        //register state 12 and unregister state 13
        val oldStateKeeper = StateKeeperDispatcher()
        assertFalse(oldStateKeeper.isRegistered(key = "Key12"))
        oldStateKeeper.register(key = "Key12", State.serializer()) { State(12) }
        oldStateKeeper.register(key = "Key13", State.serializer()) { State(12) }
        assertTrue(oldStateKeeper.isRegistered(key = "Key12"))
        assertTrue(oldStateKeeper.isRegistered(key = "Key13"))
        oldStateKeeper.unregister(key = "Key13")
        assertFalse(oldStateKeeper.isRegistered(key = "Key13"))
        val savedState = oldStateKeeper.save()
        assertNull(oldStateKeeper.consume<State>(key = "Key12"))
        assertNull(oldStateKeeper.consume<State>(key = "Key13"))

        //restore state 12
        val newStateKeeper = StateKeeperDispatcher(savedState)
        val restoredState12 = newStateKeeper.consume<State>(key = "Key12")
        assertEquals(12, restoredState12?.someValue)
        assertFalse(newStateKeeper.isRegistered(key = "Key12"))
        val restoredState13 = newStateKeeper.consume<State>(key = "Key13")
        assertNull(restoredState13)
        assertFalse(newStateKeeper.isRegistered(key = "Key13"))
    }

    /**
     * A common pattern in Decompose is to combine instance retention with state preservation.
     * * The InstanceKeeper retain object in memory and provides fast access to the retained object during configuration changes.
     * * We can keep big, non serializable object (network client) in the InstanceKeeper
     * * While the StateKeeper serialize object to ensures the data can be restored after process death.
     */
    @Test
    fun `try retained state`() {
        val instanceKeeper = InstanceKeeperDispatcher()
        val retainedState1 = instanceKeeper.getOrCreate { RetainedState(State(1)) }
        assertEquals(1, retainedState1.state.someValue)
        val retainedState2 = instanceKeeper.getOrCreate { RetainedState(State(2)) }
        assertEquals(1, retainedState2.state.someValue)

        val oldStateKeeper = StateKeeperDispatcher()
        oldStateKeeper.register(
            key = "SAVED_STATE",
            strategy = State.serializer(),
            supplier = retainedState2::state /* or { retainedState2.state }*/
        )
        val savedState = oldStateKeeper.save()

        val newStateKeeper = StateKeeperDispatcher(savedState)
        val restoredState = newStateKeeper.consume<State>("SAVED_STATE")
        assertEquals(1, restoredState?.someValue)
    }

    @Test
    fun `Saving state in a component`() {
        class SomeComponent(componentContext: ComponentContext) :
            ComponentContext by componentContext {
            // Either restore the previously saved state or create a new (initial) one
            private var state: State =
                stateKeeper.consume(key = "SAVED_STATE", strategy = State.serializer()) ?: State()

            init {
                stateKeeper.register(key = "SAVED_STATE", strategy = State.serializer()) {
                    state // Called when it's time to save the state
                }
            }
        }
    }

    fun `Retained state`() {
        class SomeComponent(componentContext: ComponentContext) :
            ComponentContext by componentContext {
            private val retainedState =
                instanceKeeper.getOrCreate {
                    RetainedState(
                        savedState = stateKeeper.consume(
                            key = "SAVED_STATE",
                            strategy = State.serializer()
                        )
                    )
                }

            init {
                // Called when it's time to save the state
                stateKeeper.register(
                    key = "SAVED_STATE",
                    strategy = State.serializer(),
                    supplier = retainedState::state
                )
            }
        }

        class RetainedCoroutineScope(mainContext: CoroutineContext) : InstanceKeeper.Instance {
            // The scope survives Android configuration changes
            private val scope = CoroutineScope(mainContext + SupervisorJob())

            fun foo() {
                scope.launch {
                    // Do the job
                }
            }

            override fun onDestroy() {
                scope.cancel() // Cancel the scope when the instance is destroyed
            }
        }
    }
}