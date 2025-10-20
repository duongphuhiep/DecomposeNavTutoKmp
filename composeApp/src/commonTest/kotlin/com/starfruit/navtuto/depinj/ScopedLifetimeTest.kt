package com.starfruit.navtuto.depinj

import org.kodein.di.*
import org.kodein.di.bindings.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ScopedLifetimeTest {
    class MyActivity(var name: String): AutoCloseable {
        var userData: ScopeRegistry? = null
        override fun close() {
            userData?.close()
            println("activity of $name is closed")
        }
    }
    class OauthManager(val context: MyActivity): AutoCloseable {
        override fun close() {
            println("oauth manager of context ${context.name} is closed")
        }
    }

    object ActivityScope : Scope<MyActivity> {
        override fun getRegistry(context: MyActivity): ScopeRegistry =
            context.userData ?: StandardScopeRegistry().also { context.userData = it }
    }

    @Test
    fun scopedLifetime() {
        val globalDi: DI = DI {
            bind<OauthManager> { scoped(ActivityScope).singleton { OauthManager(context) } }
        }

        val myActivity = MyActivity("foo")
        val scopedDi = globalDi.on(myActivity)
        val oauthManager by scopedDi.instance<OauthManager>()
        assertEquals("foo",oauthManager.context.name)

        myActivity.close()
    }
}