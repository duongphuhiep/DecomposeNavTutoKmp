package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CoroutineScopeCreation {
    /**
     * Example 1: Create a CoroutineScope which live as long as the Component
     */
    class SomeComponent1(
        componentContext: ComponentContext,
        mainContext: CoroutineContext,
        private val ioContext: CoroutineContext,
    ) : ComponentContext by componentContext {

        // The scope is automatically cancelled when the component is destroyed
        private val scope = CoroutineScope(mainContext + SupervisorJob())

        fun foo() {
            scope.launch {
                val result =
                    withContext(ioContext) {
                        "Result" // Result from background thread
                    }

                println(result) // Handle the result on main thread
            }
        }
    }


    /**
     * Example 2: a CouroutineScope which survives configuration changes
     */
    internal class SomeRetainedInstance(mainContext: CoroutineContext) : InstanceKeeper.Instance {
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

    class SomeComponent2 (
        componentContext: ComponentContext,
        mainContext: CoroutineContext,
    ) : ComponentContext by componentContext {

        private val someRetainedInstance = instanceKeeper.getOrCreate { SomeRetainedInstance(mainContext) }
    }
}