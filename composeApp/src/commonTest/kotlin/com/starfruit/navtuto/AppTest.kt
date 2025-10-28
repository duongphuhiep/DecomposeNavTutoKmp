package com.starfruit.navtuto

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.instance
import kotlin.coroutines.CoroutineContext
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

class AppTest {
    val di = ConfigurableDI().initAppDependencies(
        DI.Module("contextAndMock", allowSilentOverride = true) {
            bindSingleton<CoroutineContext>("IO") { Dispatchers.Default }
        }
    )
    val rootLifecycle = LifecycleRegistry()
    lateinit var rootComponent: RootComponent;

    @BeforeTest
    fun setup() {
        val rootComponentFactory by di.instance<RootComponent.Factory>()
        rootComponent = rootComponentFactory(DefaultComponentContext(rootLifecycle))
    }

    @Test
    fun `happy path - update text on ScreenA then go to screenB`() {
        //the current active screen is screenA
        var currentScreen = rootComponent.childStack.active.instance
        assertIs<RootComponent.Child.ScreenA>(currentScreen)

        //type "1234" on ScreenA
        currentScreen.component.updateText("1234")

        currentScreen.component.goToScreenB()

        //Now the current active screen is screenB
        currentScreen = rootComponent.childStack.active.instance
        assertIs<RootComponent.Child.ScreenB>(currentScreen)

        // the "1234" is displayed on the screenB
        assertEquals("1234", currentScreen.component.text)

        currentScreen.component.goBack()

        //the current active screen is screenA again
        currentScreen = rootComponent.childStack.active.instance
        assertIs<RootComponent.Child.ScreenA>(currentScreen)
    }

    @Test
    fun `happy path - update text on ScreenA then go to screenB assert lifecycle`() {
        // By default, root is INITIALIZED
        // uncomment this to put root to the STARTED state
        // rootLifecycle.onCreate()
        // rootLifecycle.onStart()

        //the current active screen is screenA
        val screenA = rootComponent.childStack.active.instance
        assertIs<RootComponent.Child.ScreenA>(screenA)
        assertIs<ScreenAComponent>(screenA.component)
        println("screenA=${screenA.component.lifecycle.state}")

        //type "1234" on ScreenA
        screenA.component.updateText("1234")
        println("Go to ScreenB")
        screenA.component.goToScreenB()

        //Now the current active screen is screenB
        val screenB = rootComponent.childStack.active.instance
        assertIs<RootComponent.Child.ScreenB>(screenB)
        assertIs<ScreenBComponent>(screenB.component)
        println("screenA=${screenA.component.lifecycle.state}, screenB=${screenB.component.lifecycle.state}")

        // the "1234" is displayed on the screenB
        assertEquals("1234", screenB.component.text)

        println("Go back to screenA")
        screenB.component.goBack()

        //the current active screen is screenA again
        val screenA1 = rootComponent.childStack.active.instance
        //screenB is destroyed
        assertEquals(Lifecycle.State.DESTROYED, screenB.component.lifecycle.state)
        //it is the same screenA instance
        assertEquals(screenA, screenA1)
        println("screenA=${screenA.component.lifecycle.state}, screenB=${screenB.component.lifecycle.state}")

        //go the screenB the second time
        println("Go to ScreenB the second time")
        screenA.component.goToScreenB()

        val screenB1 = rootComponent.childStack.active.instance
        assertIs<RootComponent.Child.ScreenB>(screenB1)
        assertIs<ScreenBComponent>(screenB1.component)
        assertEquals("1234", screenB1.component.text)

        // it is not the same screenB as the first one (the first one is destroyed)
        assertNotEquals(screenB.component, screenB1.component)
        println("screenA=${screenA.component.lifecycle.state}, screenB=${screenB.component.lifecycle.state}, screenB1=${screenB1.component.lifecycle.state}")
    }
}

