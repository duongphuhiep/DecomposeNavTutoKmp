package com.starfruit.navtuto

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        val component = remember { DefaultRootComponent(DefaultComponentContext(LifecycleRegistry())) }
        RootView(component)
    }
}