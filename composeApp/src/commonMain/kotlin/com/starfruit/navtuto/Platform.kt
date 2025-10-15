package com.starfruit.navtuto

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform