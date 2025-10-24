package com.starfruit.util

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface SuspendLazy<out T> : suspend () -> T {
    val isInitialized: Boolean
}

/**
 * Creates a SuspendLazy that uses the specified initializer function.
 * Thread-safe and ensures the initializer is called at most once.
 * The initializer is garbage collected after the first invocation.
 */
fun <T> suspendLazy(initializer: suspend () -> T): SuspendLazy<T> {
    return SynchronizedSuspendLazy(initializer)
}

private class SynchronizedSuspendLazy<out T>(
    initializer: suspend () -> T
) : SuspendLazy<T> {
    private val mutex = Mutex()
    private var _initializer: (suspend () -> T)? = initializer
    private var _value: Any? = UNINITIALIZED

    override val isInitialized: Boolean
        get() = _value !== UNINITIALIZED

    @Suppress("UNCHECKED_CAST")
    override suspend fun invoke(): T {
        // Fast path - value already computed
        val v1 = _value
        if (v1 !== UNINITIALIZED) {
            return v1 as T
        }

        // Slow path - need to compute
        return mutex.withLock {
            val v2 = _value
            if (v2 !== UNINITIALIZED) {
                // Another coroutine computed it while we waited
                v2 as T
            } else {
                // We're the first, compute the value
                val computed = _initializer!!()
                _value = computed
                _initializer = null // Allow GC of the initializer
                computed
            }
        }
    }
}

private object UNINITIALIZED
