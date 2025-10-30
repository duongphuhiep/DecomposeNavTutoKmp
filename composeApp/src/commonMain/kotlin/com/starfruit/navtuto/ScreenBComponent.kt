package com.starfruit.navtuto

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.starfruit.util.Optional
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

suspend fun loadData(): String {
    delay(5000)
    return "I'm the Loading's result"
}

interface IScreenBComponent {
    val text: String

    val someData: Value<Optional<String>>
    val loadingState: Value<String>
    fun loadSomeData(scope: CoroutineScope)

    val waiterModalComponent: IWaiterModalComponent

    fun showProgression()

    fun goBack()
}

class ScreenBComponent(
    componentContext: ComponentContext,
    override val text: String,
    private val onGoBack: () -> Unit,
    waiterModalComponentFactory: WaiterModalComponent.Factory,
    private val mainCoroutineContext: CoroutineContext
) : ComponentContext by componentContext, IScreenBComponent {
    class Factory(
        private val waiterModalComponentFactory: WaiterModalComponent.Factory,
        private val globalCoroutineContext: CoroutineContext
    ) {
        operator fun invoke(
            componentContext: ComponentContext,
            text: String,
            onGoBack: () -> Unit
        ): ScreenBComponent = ScreenBComponent(
            componentContext = componentContext,
            text = text,
            onGoBack = onGoBack,
            waiterModalComponentFactory = waiterModalComponentFactory,
            mainCoroutineContext = globalCoroutineContext
        )
    }

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

    val progression = MutableValue("")
    override val waiterModalComponent = waiterModalComponentFactory(
        childContext(key = "waiter", lifecycle = LifecycleRegistry()),
        progression
    )

    //this scope live as long as the Component
    private val componentScope = coroutineScope(mainCoroutineContext + SupervisorJob())

    override fun showProgression() {
        componentScope.launch {
            progression.value = "initialize"
            delay(1.seconds)
            for (i in 1..10) {
                progression.value = "$i%"
                delay(300)
                Logger.d("run $i%")
            }
            progression.value = ""
            Logger.i("run finished")
        }
    }

    override fun goBack() = onGoBack()
}
