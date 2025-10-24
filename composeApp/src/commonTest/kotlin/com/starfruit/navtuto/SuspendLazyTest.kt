package com.starfruit.navtuto

import com.starfruit.util.suspendLazy
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SuspendLazyTest {
    @Test
    fun `Basic usage with invoke operator`() = runTest {
        val expensive = suspendLazy {
            println("Computing expensive value...")
            kotlinx.coroutines.delay(1000)
            42
        }

        assertFalse { expensive.isInitialized }
        assertEquals(42, expensive())
        assertTrue { expensive.isInitialized }
        assertEquals(42, expensive()) //no recomputation
    }

    @Test
    fun `With suspend API calls`() = runTest {
        val userData = suspendLazy {
            fetchUserFromApi()
        }

        // Value is only fetched when first accessed
        val user = userData()
        println("User: $user")

        // Example 3: Can be used as a suspend function
        val processor = suspendLazy { loadProcessor() }
        processWithSuspend(processor)
    }

    suspend fun processWithSuspend(lazyValue: suspend () -> String) {
        println("Processing: ${lazyValue()}")
    }

    suspend fun fetchUserFromApi(): String {
        kotlinx.coroutines.delay(500)
        return "User-123"
    }

    suspend fun loadProcessor(): String {
        return "Processor initialized"
    }
}