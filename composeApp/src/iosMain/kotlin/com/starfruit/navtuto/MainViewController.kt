package com.starfruit.navtuto

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlin.test.todo

fun MainViewController() = ComposeUIViewController {
    val component = remember { DefaultRootComponent(DefaultComponentContext(LifecycleRegistry())) }
    App(component)
}