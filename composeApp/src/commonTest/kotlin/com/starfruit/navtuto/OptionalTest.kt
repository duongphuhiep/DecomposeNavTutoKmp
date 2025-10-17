package com.starfruit.navtuto

import com.starfruit.util.Optional
import kotlin.reflect.KProperty
import kotlin.test.*

class OptionalTest {
    @Test
    fun `Optional with concrete value - simple use case`() {
        val a: Optional<Int> = Optional(10)
        assertEquals(10, a.value)
        assertNotNull(a.value)

        val b: Optional<Int> = Optional { null }
        assertNull(b.value)
    }

    @Test
    fun `Equals hashcode`() {
        val a1: Optional<Int> = Optional(10)
        val a2: Optional<Int> = Optional { 10 }
        val b1: Optional<Int> = Optional { null }
        val b2 = Optional(null)
        val s1: Optional<String> = Optional(null)
        val s2: Optional<String> = Optional { null }
        val s3: Optional<String> = Optional { "10" }

        assertEquals(a1, a2)
        assertEquals(b1, b2)
        assertNotEquals(a1, b2)
        assertNotEquals(a2, b1)

        assertTrue { b1 == s1 }
        assertTrue { b1 == s2 }
        assertFalse { a2 == s3 }
    }

    @Test
    fun `Optional with provider - simple use case`() {
        val a: Optional<Int> = Optional { 10 }
        assertEquals(10, a.value)
        assertNotNull(a.value)

        val b: Optional<Int> = Optional { null }
        assertNull(b.value)
    }

    @Test
    fun `Optional with provider - wrap around lazy`() {
        var expensiveCalculationIsCalled = false
        fun expensiveCalculation(): Int {
            expensiveCalculationIsCalled = true
            return 10
        }
        //x is a delegate property
        val x by lazy { expensiveCalculation() }
        //a should wrap around x without evaluating it
        val a: Optional<Int> = Optional { x }
        assertFalse { expensiveCalculationIsCalled } // failed
        // it would only evaluate when the value is access or whenever equals / hashcodes is called
        assertEquals(10, a.value)
        assertTrue { expensiveCalculationIsCalled }
    }

    @Test
    fun `Optional with provider - other delegate property`() {
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
        val a: Optional<Int> = Optional { x }
        assertEquals(1, a.value)
        assertEquals(2, a.value)
        assertEquals(3, a.value)
    }
}