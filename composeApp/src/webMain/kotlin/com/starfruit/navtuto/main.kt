package com.starfruit.navtuto

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.kodein.di.DI
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.instance
import kotlin.getValue

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val di = ConfigurableDI().initAppDependencies(
        DI.Module("context") {}
    )
    val rootComponentFactory by di.instance<RootComponent.Factory>()

    ComposeViewport {
        val rootLifecycle = LifecycleRegistry()
        val component = remember { rootComponentFactory(DefaultComponentContext(rootLifecycle)) }
        RootView(component)
    }
}