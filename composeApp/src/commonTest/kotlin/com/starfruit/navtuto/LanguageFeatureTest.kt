package com.starfruit.navtuto

import kotlin.test.Test
import kotlin.test.assertEquals

class LanguageFeatureTest {

    class SomeA(/*Primary constructor*/ val m: String = "default") {
        /**
         * Belongs to the primary constructor. Runs every time an instance is created.
         * Can access parameters from the primary constructor directly.
         * A class can have multiple init blocks, executed in the order they appear.
         */
        init {
            println("init")
            initCounter++
        }

        /**
         * Secondary constructor.
         * Useful when you need different ways to initialize a class.
         * Calls the primary constructor.
         * You cannot access primary constructor parameters directly unless you delegate to it.
         */
        constructor(n: Int=1) : this(n.toString()) {
            println("ctor")
            ctorCounter++
        }

        companion object {
            var initCounter = 0;
            var ctorCounter = 0;
        }
    }

    @Test
    fun initVsConstructorTest() {
        SomeA("hello")
        SomeA(123)
        assertEquals(1, SomeA.ctorCounter)
        assertEquals(2, SomeA.initCounter)
    }
}