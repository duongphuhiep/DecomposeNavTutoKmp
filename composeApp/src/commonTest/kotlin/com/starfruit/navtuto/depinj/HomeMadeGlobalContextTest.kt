package com.starfruit.navtuto.depinj

import org.kodein.di.*
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeMadeGlobalContextTest {
    /**
     * equivalent to Koin's GlobalContext
     */
    object GlobalContext {
        lateinit var di: DI
            private set

        /**
         * equivalent to startKoin {}
         */
        fun init(applicationContextModule: DI.Module, commonMainModule: DI.Module, platformModule: DI.Module) {
            di = DI {
                import(applicationContextModule)
                import(commonMainModule)
                import(platformModule) //expect/actual
            }
        }
    }

    class ApplicationContext(val name: String)
    class AndroidPlatform(val appcontext: ApplicationContext)
    class CommonMain(val platform: AndroidPlatform)

    @Test
    fun globalContextEquivalent() {
        //MainApplication.onCreate()
        GlobalContext.init(
            DI.Module("context"){ bindSingleton { ApplicationContext("hello") } },
            DI.Module("common"){ bindSingleton { CommonMain(instance()) } },
            DI.Module("android"){ bindSingleton { AndroidPlatform(instance()) } }
        )

        val commonMain by GlobalContext.di.instance<CommonMain>()
        assertEquals("hello", commonMain.platform.appcontext.name)
    }
}