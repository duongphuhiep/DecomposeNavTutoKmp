package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.lifecycle.*
import com.arkivanov.essenty.lifecycle.Lifecycle.Callbacks
import kotlin.test.Test
import kotlin.test.assertEquals

class ComponentTest {
    @Test
    fun `child component`() {
        class Counter(componentContext: ComponentContext) : ComponentContext by componentContext

        class SomeParent(componentContext: ComponentContext) :
            ComponentContext by componentContext {
            /**
             * This thing is useful for parent to control the child (counter) lifecycle.
             * The parent should never destroy the lifecycle of a permanent child component!
             * It is not the real lifecycle of the Counter component.
             */
            private val counterLifecycleRegistry = LifecycleRegistry(Lifecycle.State.RESUMED).apply {
                /* this lifecycle registration won't aware about the parent lifecycle */
                doOnCreate { println("Counter initial lifecycle is Created") }
                doOnStart { println("Counter initial lifecycle is Started") }
                doOnResume { println("Counter initial lifecycle is Resumed") }
                doOnPause { println("Counter initial lifecycle is Paused") }
                doOnStop { println("Counter initial lifecycle is Stopped") }
                doOnDestroy { println("Counter initial lifecycle is Destroyed") }
            }
            /**
             * A permanent child component should be always instantiated during the initialisation of the parent, and it is automatically destroyed at the end of the parent's lifecycle. It is possible to manually control the lifecycle of a permanent child component, e.g. resume it, pause or stop. But permanent child components must never be destroyed manually.
             */
            val counter: Counter = Counter(childContext(key = "Counter", counterLifecycleRegistry)).apply {
                /* this is the real lifecycle of counter, it merges the lifecycle of the Parent and so aware about parent stop or destroyed */
                doOnCreate { println("🔔 Counter is Created") }
                doOnStart { println("🔔 Counter is Started") }
                doOnResume { println("🔔 Counter is Resumed") }
                doOnPause { println("🔔 Counter is Paused") }
                doOnStop { println("🔔 Counter is Stopped") }
                doOnDestroy { println("🔔 Counter is Destroyed") }
            }

            /**
             * control the lifecycle of a permanent child component,
             */
            fun onPauseCounter() {
                counterLifecycleRegistry.onPause()
            }

            /**
             * control the lifecycle of a permanent child component,
             */
            fun onResumeCounter() {
                counterLifecycleRegistry.onResume()
            }
        }

        val lifecycle = LifecycleRegistry(Lifecycle.State.RESUMED)
        val context = DefaultComponentContext(lifecycle)
        val parent = SomeParent(context)

        parent.onPauseCounter();
        assertEquals(Lifecycle.State.STARTED, parent.counter.lifecycle.state)
        parent.onResumeCounter();
        assertEquals(Lifecycle.State.RESUMED, parent.counter.lifecycle.state)

        assertEquals(Lifecycle.State.RESUMED, parent.lifecycle.state)

        lifecycle.onPause()
        assertEquals(Lifecycle.State.STARTED, parent.lifecycle.state)
        assertEquals(Lifecycle.State.STARTED, parent.counter.lifecycle.state)

        lifecycle.onStop()
        assertEquals(Lifecycle.State.CREATED, parent.lifecycle.state)
        assertEquals(Lifecycle.State.CREATED, parent.counter.lifecycle.state)

        lifecycle.onDestroy()
        assertEquals(Lifecycle.State.DESTROYED, parent.lifecycle.state)
        assertEquals(Lifecycle.State.DESTROYED, parent.counter.lifecycle.state)
    }
}