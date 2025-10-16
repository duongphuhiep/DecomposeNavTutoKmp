package com.starfruit.navtuto

import com.starfruit.util.OptionalWrapper
import kotlin.reflect.KProperty
import kotlin.test.*

class OptionalTest {
    @Test
    fun `simple use case`() {
        val a: OptionalWrapper<Int> = OptionalWrapper { 10 }
        assertEquals(10, a.value)
        assertNotNull(a.value)

        val b: OptionalWrapper<Int> = OptionalWrapper { null }
        assertNull(b.value)
    }

    @Test
    fun `optional wrap around lazy`() {
        var expensiveCalculationIsCalled = false
        fun expensiveCalculation(): Int {
            expensiveCalculationIsCalled = true
            return 10
        }
        //x is a delegate property
        val x by lazy { expensiveCalculation() }
        //a should wrap around x without evaluating it
        val a: OptionalWrapper<Int> = OptionalWrapper { x }
        assertFalse { expensiveCalculationIsCalled } // failed
        // it would only evaluate when the value is access or whenever equals / hashcodes is called
        assertEquals(10, a.value)
        assertTrue { expensiveCalculationIsCalled }
    }

    @Test
    fun `option wrap around other delegate property`() {
        var countRead = 0;
        var countWrite = 0;
        class mydelegate {
            operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
                println("Reading times: ${++countRead} - $thisRef/${property.name}")
                return countRead
            }

            operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
                println("Writing times: ${++countWrite} - $thisRef/${property.name} - I don't care about the value $value")
            }
        }

        var x by mydelegate()
        x = 10
        x = 11
        val a: OptionalWrapper<Int> = OptionalWrapper { x }
        assertEquals(1, a.value)
        assertEquals(2, a.value)
        assertEquals(3, a.value)
    }
}