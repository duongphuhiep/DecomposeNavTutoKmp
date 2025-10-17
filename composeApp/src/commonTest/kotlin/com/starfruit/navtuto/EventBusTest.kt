package com.starfruit.navtuto

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventBusTest {

    object SimpleEventBus {
        private val _events = MutableSharedFlow<Any>()
        val events: SharedFlow<Any> = _events.asSharedFlow()
        suspend fun post(event: Any) {
            _events.emit(event)
            println("⚡ event emitted $event")
        }
    }

    sealed class MyEvent {
        data class UserLoggedIn(val userId: String) : MyEvent()
        object ThemeChanged : MyEvent()
    }

    @Test
    fun `subscribe to events`() = runTest(UnconfinedTestDispatcher()) {
        val job = launch {
            //the UnconfinedTestDispatcher tells to enter here eagerly (before the below EventBus.post is called)
            //without UnconfinedTestDispatcher, the event is post before the collect starts
            SimpleEventBus.events.collect { event ->
                when (event) {
                    is MyEvent.UserLoggedIn -> println("👮 User ${event.userId} logged in!")
                    is MyEvent.ThemeChanged -> println("🖼 Theme changed!")
                }
            }
        }

        SimpleEventBus.post(MyEvent.UserLoggedIn("123"))
        SimpleEventBus.post(MyEvent.ThemeChanged)
        //advanceUntilIdle()
        job.cancel() //stop collect
    }

}