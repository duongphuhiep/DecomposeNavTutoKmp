package com.starfruit.navtuto

import android.app.Application
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.conf.global

class MainApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        DI.global.initAppDependencies(DI.Module("context") {
            bindSingleton<Context> { this@MainApplication }
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel()
    }
}
