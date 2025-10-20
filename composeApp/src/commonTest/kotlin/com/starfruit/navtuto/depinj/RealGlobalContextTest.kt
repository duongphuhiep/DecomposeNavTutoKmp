package com.starfruit.navtuto.depinj

import org.kodein.di.*
import org.kodein.di.conf.DIGlobalAware
import org.kodein.di.conf.global
import kotlin.test.Test
import kotlin.test.assertEquals

class RealGlobalContextTest {
    class ApplicationContext(val name: String)
    class AndroidPlatform(val appcontext: ApplicationContext)
    class CommonMain(val platform: AndroidPlatform)

    class MyManager() : DIGlobalAware {
        val ds: CommonMain by instance()
    }

    @Test
    fun globalContextEquivalent() {
        DI.global.addImport(DI.Module("context"){ bindSingleton { ApplicationContext("hello") } })
        DI.global.addImport(DI.Module("common"){ bindSingleton { CommonMain(instance()) } })
        DI.global.addImport(DI.Module("android"){ bindSingleton { AndroidPlatform(instance()) } })

        val commonMain by DI.global.di.instance<CommonMain>()
        assertEquals("hello", commonMain.platform.appcontext.name)

        val myManager = MyManager()
        assertEquals("hello", myManager.ds.platform.appcontext.name)
    }
}