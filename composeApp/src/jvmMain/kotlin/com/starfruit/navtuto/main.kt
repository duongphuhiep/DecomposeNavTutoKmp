package com.starfruit.navtuto

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "NavTuto",
    ) {
        val component = remember { DefaultRootComponent(DefaultComponentContext(LifecycleRegistry())) }
        App(component)
    }
}