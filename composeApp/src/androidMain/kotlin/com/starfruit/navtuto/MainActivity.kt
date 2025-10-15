package com.starfruit.navtuto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.retainedComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val rootComponent = retainedComponent {
            DefaultRootComponent(it)
        }
        setContent {
            App(rootComponent)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val rootComponent = DefaultRootComponent(DefaultComponentContext(LifecycleRegistry()))
    App(rootComponent)
}