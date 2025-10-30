package com.starfruit.navtuto

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.kodein.di.DI
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.instance

// Create a CompositionLocal
val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

@Composable
fun RootView(component: IRootComponent) {
    MaterialTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState)
        {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { contentPadding ->
                Column(
                    Modifier
                        .padding(contentPadding)
                        .fillMaxSize()
                ) {
                    val childStack by component.childStack.subscribeAsState()
                    Children(
                        stack = childStack,
                        modifier = Modifier.safeContentPadding(),
                        animation = stackAnimation(slide()),
                    ) {
                        when (val instance = it.instance) {
                            is IRootComponent.Child.ScreenA -> ScreenAView(instance.component)
                            is IRootComponent.Child.ScreenB -> ScreenBView(instance.component)
                            is IRootComponent.Child.Pages -> PagesView(instance.component)
                            is IRootComponent.Child.Panels -> PanelsView(instance.component)
                            is IRootComponent.Child.List -> ListView(instance.component)
                        }
                    }
                }
            }
        }
    }
}

val componentContextPreview = DefaultComponentContext(LifecycleRegistry())

val rootComponentPreview: RootComponent
    get() {
        val di = ConfigurableDI().initAppDependencies(
            DI.Module("preview") {}
        )
        val rootComponentFactory by di.instance<RootComponent.Factory>()
        return rootComponentFactory(componentContextPreview)
    }

@Composable
@Preview(showBackground = true, widthDp = 480, heightDp = 800)
private fun RootPreview() {
    RootView(rootComponentPreview)
}
