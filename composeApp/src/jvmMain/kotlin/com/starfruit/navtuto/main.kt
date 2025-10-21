package com.starfruit.navtuto

import androidx.compose.ui.window.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.starfruit.util.runOnUiThread
import org.kodein.di.DI
import org.kodein.di.conf.global
import org.kodein.di.instance

fun main() {
    val lifecycle = LifecycleRegistry()

    val di = DI.global.initAppDependencies(DI.Module("context"){})
    val rootComponentFactory by di.instance<RootComponent.Factory>()
    val root =
        runOnUiThread {
            rootComponentFactory(DefaultComponentContext(lifecycle))
        }

    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            title = "NavTuto",
            state = windowState) {
            RootView(root)
        }
    }
}

//fun main2() = application {
//    val lifecycle = LifecycleRegistry()
//    val windowState = rememberWindowState()
//    LifecycleController(lifecycle, windowState)
//
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "NavTuto",
//    ) {
//        val component = remember { DefaultRootComponent(DefaultComponentContext(lifecycle)) }
//        RootView(component)
//    }
//}