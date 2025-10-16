package com.starfruit.util

/**
 * Wrap around concrete value
 */
class Optional<out T: Any> (val value: T?): Any() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Optional<*>) return false
        return value == other.value
    }
    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }
}

/**
 * Wrap around a value provider, evaluate the value each time needed
 */
class OptionalWrapper<out T: Any> (private val provider: () -> T?): Any() {
    val value: T? get() = provider()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OptionalWrapper<*>) return false
        return value == other.value
    }
    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }
}