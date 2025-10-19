package com.starfruit.navtuto

import androidx.compose.runtime.remember
import androidx.compose.ui.window.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.starfruit.util.runOnUiThread

fun main() {
    val lifecycle = LifecycleRegistry()

    val root =
        runOnUiThread {
            DefaultRootComponent(DefaultComponentContext(lifecycle))
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