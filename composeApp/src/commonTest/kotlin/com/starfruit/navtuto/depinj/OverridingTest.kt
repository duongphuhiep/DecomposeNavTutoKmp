package com.starfruit.navtuto.depinj

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.instance
import kotlin.getValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * https://kosi-libs.org/kodein/7.25/core/modules-inheritance.html#_overriding
 * https://kosi-libs.org/kodein/7.25/extension/configurable.html
 */
class OverridingTest {
    interface A {
        val name: String
    }

    class RealA(override val name: String = "Real") : A
    class MockA(override val name: String = "Mock") : A

    /**
     * try configurable DI container.
     * with configurableDI we can setup the DI container everywhere,
     * no need a central place which gather all the binding.
     * But on the first access of the underlying DI container, then
     * the configurableDI will be locked, we can no longer configure it.
     * https://kosi-libs.org/kodein/7.25/extension/configurable.html
     */
    @Test
    fun tryConfiguredDi() {
        val configurableDi = ConfigurableDI()
            .addConfig {
                bindSingleton<A> { RealA("Real") }
            }

        /**
         * access to the underlying container `configurableDi.di` will
         * make the `configurableDi` locked on this point
         */
        val a1 by configurableDi.instance<A>()
        assertEquals("Real", a1.name)

        //if we try to change the config again then IllegalStateException
        assertFailsWith(IllegalStateException::class) {
            configurableDi.addConfig {
                bindSingleton<A>(overrides = true) { MockA("Mock") }
            }
        }
    }

    /**
     * in case the application exposes all the DI.Module then it is also straight forward
     * to build our test-di container and overrides things with mocks
     */
    @Test
    fun overridingTest_WithModule() {
        val appModule = DI.Module("app") {
            bindSingleton<A> { RealA("Real") }
        }

        val testDi = DI {
            import(appModule)
            bindSingleton<A>(overrides = true) { MockA("Mock") }
        }

        val a1 by testDi.instance<A>()
        assertEquals("Mock", a1.name)
    }

    /**
     * In case the application exposes a [ConfigurableDI] then it is also straight forward.
     * to build our test-di container and overrides things with mocks. The problem is that
     * the application usually expose a global singleton [ConfigurableDI], if it is locked
     * by one test then other tests are in trouble.
     */
    @Test
    fun overridingTest_WithConfiguredDi() {
        // the application layer finished the DI setup (with real components)
        val configurableDi = ConfigurableDI()
            .addConfig {
                bindSingleton<A> { RealA("Real") }
            }

        /**
         * suppose that the configurableDi is only be locked by the real entry point
         * (eg. ApplicationStart). When we run the test, the real entry point is not called
         * so the configurableDi is still configurable.
         * we will add some mock binding to override the real one for the test
         */
        configurableDi.addConfig {
            bindSingleton<A>(overrides = true) { MockA("Mock") }
        }

        /**
         * access to the underlying container `configurableDi.di` will
         * make the `configurableDi` locked at this point
         */
        val a1 by configurableDi.instance<A>()
        assertEquals("Mock", a1.name)
    }

}