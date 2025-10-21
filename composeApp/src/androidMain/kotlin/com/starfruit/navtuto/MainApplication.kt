package com.starfruit.navtuto

import android.app.Application
import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.conf.global

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DI.global.initAppDependencies(DI.Module("context") {
            bindSingleton<Context> { this@MainApplication }
        })
    }
}
