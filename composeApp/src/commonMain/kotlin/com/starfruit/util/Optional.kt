package com.starfruit.util

/**
 * A class that wraps either a concrete value or a value provider, allowing nullable values.
 * Can be instantiated with a direct value `Optional(value)` or a provider function `Optional { value }`.
 *
 * A `Optional<Something>` is equivalent to `Something?`. It is recommended to use `Something?`
 * whenever possible.
 *
 * One use case which `Something?` isn't possible is the `Value<T: Any>` defined
 * in the decompose library. `Value<Something?>` won't compiled, you will have to use `Value<Optional<Something>>`
 * as replacement.
 *
 * Warning: `Optional<Foo>(null) == Optional<Bar>(null)` they are `equals` although
 * the types are different, because they are holding a null value which is types-agnostic.
 */
class Optional<out T : Any> private constructor(
    private val _value: T? = null,
    private val _provider: (() -> T?)? = null
) : Any() {
    /**
     * Constructs an Optional with a direct value.
     */
    constructor(value: T?) : this(_value = value, _provider = null)

    /**
     * Constructs an Optional with a provider function.
     */
    constructor(provider: () -> T?) : this(_value = null, _provider = provider)

    /**
     * Retrieves the value, either directly or by invoking the provider.
     */
    val value: T?
        get() = _provider?.invoke() ?: _value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Optional<*>) return false
        return value == other.value
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }
}