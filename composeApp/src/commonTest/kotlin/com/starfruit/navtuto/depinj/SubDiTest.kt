package com.starfruit.navtuto.depinj

import org.kodein.di.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SubDiTest {
    class SomeA(val name: String) {
    }

    interface DbDriver {
        val activityContextName: String
    };

    class ActivityContext(val name: String = "ActivityContext ${id++}") : DIAware {
        override val di: DI by subDI(commonDi) {
            // This makes 'this Activity' available as context for contexted bindings
            bind<ActivityContext>() with instance(this@ActivityContext)
        }
        val driver: DbDriver by di.instance()
    }

    data class AndroidDbDriver(val activityContext: ActivityContext) : DbDriver {
        override val activityContextName = activityContext.name
    }

    companion object {
        var id: Int = 0

        val commonDi: DI = DI {
            bind<DbDriver>() with contexted<ActivityContext>().provider {
                AndroidDbDriver(context)
            }
            bind<SomeA> { provider { SomeA("SomeA ${id++}") } }
        }
    }

    @Test
    fun trySubDi() {
        val activity1 = ActivityContext("hello")
        assertEquals(activity1.name, activity1.driver.activityContextName)
    }

    @Test
    fun onContextTest() {
        val subDI = commonDi.on(context = ActivityContext("hello")).di
        val driver: DbDriver by subDI.instance()
        assertEquals("hello", driver.activityContextName)
    }

    @Test
    fun retrieveProviderTest() {
        fun getSomeA(): SomeA {
            val v: SomeA by commonDi.instance()
            return v
        }
        val someAProvider: () -> SomeA by commonDi.provider()

        assertEquals("SomeA 0", getSomeA().name)
        assertEquals("SomeA 1", getSomeA().name)
        assertEquals("SomeA 2", someAProvider().name)
        assertEquals("SomeA 3", someAProvider().name)
    }

}