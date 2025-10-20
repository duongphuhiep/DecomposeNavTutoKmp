package com.starfruit.navtuto

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.starfruit.navtuto.data.PlayerDb
import org.kodein.di.DI.Module
import org.kodein.di.bindProvider

actual val platformModule = Module("native") {
    bindProvider<SqlDriver> { NativeSqliteDriver(PlayerDb.Schema, "player.db") }
}
