package com.starfruit.navtuto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import org.kodein.di.DI
import org.kodein.di.conf.global
import org.kodein.di.instance

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // create a DI container scoped to the activity
        // val scopedDi = DI.global.di.on(this)
        // then use this `scopedDi` to resolve the RootComponent instead of the `DI.global.di`
        // if we do that then it is possible to inject the current activity into the Components
        // checkout the [ScopedLifetimeTest]

        val rootComponentFactory by DI.global.di.instance<RootComponent.Factory>()
        val rootComponent = retainedComponent {
            rootComponentFactory(it)
        }
        setContent {
            RootView(rootComponent)
        }
    }
}