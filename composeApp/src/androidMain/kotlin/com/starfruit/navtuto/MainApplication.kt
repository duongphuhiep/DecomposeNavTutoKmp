package com.starfruit.navtuto

import android.app.Application
import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bindSingleton

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initGlobalDi(DI.Module("context") {
            bindSingleton<Context> { this@MainApplication }
        })
    }
}
