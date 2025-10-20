package com.starfruit.navtuto

import org.kodein.di.DI

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val platformModule: DI.Module