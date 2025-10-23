package com.starfruit.navtuto

import org.kodein.di.DI

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

/**
 * Provide Platform specifics dependencies such as
 * SqlDriver, Dispatchers.IO
 */
expect val platformModule: DI.Module