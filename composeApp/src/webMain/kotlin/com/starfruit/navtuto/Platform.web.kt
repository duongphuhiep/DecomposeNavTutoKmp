package com.starfruit.navtuto

import app.cash.sqldelight.db.SqlDriver
import org.kodein.di.DI.Module
import org.kodein.di.bindProvider

actual val platformModule = Module("web") {
    bindProvider<SqlDriver> { TODO("implement me") }
}