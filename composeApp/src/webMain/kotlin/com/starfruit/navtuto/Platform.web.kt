package com.starfruit.navtuto

import app.cash.sqldelight.db.SqlDriver
import org.kodein.di.DI.Module
import org.kodein.di.bindSingleton

actual val platformModule = Module("web") {
    bindSingleton<SqlDriver> { TODO("implement me") }
}