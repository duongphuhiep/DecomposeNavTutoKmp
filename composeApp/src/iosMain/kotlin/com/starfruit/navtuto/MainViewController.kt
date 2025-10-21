package com.starfruit.navtuto

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.kodein.di.DI
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.instance
import kotlin.getValue

fun MainViewController() = ComposeUIViewController {
    val di = ConfigurableDI().initAppDependencies(
        DI.Module("context") {}
    )
    val rootComponentFactory by di.instance<RootComponent.Factory>()
    val rootLifecycle = LifecycleRegistry()

    val component = remember { rootComponentFactory(DefaultComponentContext(rootLifecycle)) }
    RootView(component)
}