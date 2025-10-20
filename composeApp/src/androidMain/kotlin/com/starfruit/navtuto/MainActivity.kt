package com.starfruit.navtuto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.retainedComponent
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.subDI

class MainActivity : ComponentActivity()/*, DIAware*/ {
//    override val di: DI by subDI(AppDi) {
//        // This makes 'this Activity' available as context for contexted bindings
//        bind<ComponentActivity>() with instance(this@MainActivity)
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val rootComponent = retainedComponent {
            DefaultRootComponent(it)
        }
        setContent {
            RootView(rootComponent)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val rootComponent = DefaultRootComponent(DefaultComponentContext(LifecycleRegistry()))
    RootView(rootComponent)
}