package com.starfruit.navtuto.notused

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

/**
 * Pattern 1: The direct method approach is generally preferable.
 *
 * Use for most cases:
 * - Better type safety - Compiler catches incorrect calls immediately
 * - More discoverable - IDE autocomplete shows all available actions
 * - Simpler mental model - Direct method calls are easier to understand
 * - Less boilerplate - No need to define sealed Event hierarchies
 * - Better for UI layer - Compose functions can call methods directly without wrapping in events
 */
interface ScreenA1Component {
    val text: Value<String>
    fun updateText(text: String)
    fun openDialogX()
    //implementation is provided from parent
    fun goToScreenB()
    //implementation is provided from parent
    fun goBack()
}

class DefaultScreenA1Component(
    componentContext: ComponentContext,
    private val onGoToScreenB: (text: String) -> Unit,
    private val onGoBack: () -> Unit,
) : ScreenA1Component, ComponentContext by componentContext {
    val _text = MutableValue("")
    override val text: Value<String> = _text

    override fun updateText(text: String) {
        _text.value = text
    }
    override fun openDialogX() {
        showDialog()
    }
    override fun goToScreenB() {
        //parent knows how to do it
        onGoToScreenB(text.value)
    }
    override fun goBack() {
        //parent knows how to do it
        onGoBack()
    }
}

/**
 * Pattern 2: More align with Model-View-Intent / Uni-directional-Flow, with a Reducer-like approach.
 *
 * Use it when:
 * - Middleware/logging needs - Single onEvent entry point makes it easy to log all user actions
 * - Event replay/undo - Events can be stored and replayed
 * - Complex state machines - When you need centralized event processing
 * - Testing focus - Easier to test by verifying event sequences rather than mocking multiple methods
 *
 * for scenarios where you genuinely need the "everything goes through one pipe" architecture - such as audit logging, analytics tracking, or when building a time-travel debugger.
 */
interface ScreenA2Component {
    val text: Value<String>

    sealed interface Event {
        data object onGoToScreenB: Event
        data class onUpdateText(val text: String): Event
        data object onGoBack: Event
        data object openDialogX: Event
    }
}
class DefaultScreenA2Component(
    componentContext: ComponentContext,
    private val onGoToScreenB: (text: String) -> Unit,
    private val onGoBack: () -> Unit,
) : ScreenA2Component, ComponentContext by componentContext, EventHandler<ScreenA2Component.Event> {
    val _text = MutableValue("")
    override val text: Value<String> = _text

    override fun onEvent(event: ScreenA2Component.Event) {
        when (event) {
            is ScreenA2Component.Event.onUpdateText -> _text.value = event.text
            ScreenA2Component.Event.onGoBack -> onGoBack()
            ScreenA2Component.Event.onGoToScreenB -> onGoToScreenB(text.value)
            ScreenA2Component.Event.openDialogX -> showDialog()
        }
    }
}

sealed interface EventHandler<T> {
    fun onEvent(event: T)
}
fun showDialog() {};