package com.starfruit.navtuto

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.starfruit.util.Optional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun  loadData(): String {
    delay(5000)
    return "I'm the Loading's result"
}

interface ScreenBComponent {
    val text: String

    val someData: Value<Optional<String>>
    val loadingState: Value<String>
    fun loadSomeData(scope: CoroutineScope)

    fun goBack()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            text: String,
            onGoBack: () -> Unit,
        ): ScreenBComponent
    }
}

class DefaultScreenBComponent private constructor(
    componentContext: ComponentContext,
    override val text: String,
    private val onGoBack: () -> Unit,
) : ScreenBComponent, ComponentContext by componentContext {
    private val _loadingState = MutableValue("not started")
    override val loadingState: Value<String> = _loadingState
    private val _someData = MutableValue<Optional<String>>(Optional(null))
    override val someData: Value<Optional<String>> = _someData

    override fun loadSomeData(scope: CoroutineScope) {
        _loadingState.value = "loading..."
        scope.launch {
            _someData.value = Optional(loadData())
            _loadingState.value = "loaded"
            Logger.d("💯 Finished loading")
        }
    }

    override fun goBack() = onGoBack()

    class Factory(): ScreenBComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            text: String,
            onGoBack: () -> Unit,
        ): ScreenBComponent = DefaultScreenBComponent(
            componentContext = componentContext,
            text = text,
            onGoBack = onGoBack,
        )
    }
}
