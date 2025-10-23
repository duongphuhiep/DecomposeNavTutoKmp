package com.starfruit.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * convert to ISO 8601 UTC date time string.
 *
 * ```
 * val tomorrow = Clock.System.now().plus(1, DateTimeUnit.DAY, TimeZone.UTC)
 * tomorrow.formatUtcIso() // "2023-03-15T13:45:30.123"
 * ```
 */
@OptIn(ExperimentalTime::class)
fun Instant.formatUtcIso(): String =
    LocalDateTime.Formats.ISO.format(this.toLocalDateTime(TimeZone.UTC))