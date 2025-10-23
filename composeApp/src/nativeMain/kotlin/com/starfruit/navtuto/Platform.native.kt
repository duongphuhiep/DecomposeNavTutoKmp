package com.starfruit.navtuto

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.starfruit.navtuto.data.PlayerDb
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI.Module
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import kotlin.coroutines.CoroutineContext

actual val platformModule = Module("native") {
    bindSingleton<CoroutineContext> { Dispatchers.Default }
    bindProvider<SqlDriver> { NativeSqliteDriver(PlayerDb.Schema.synchronous(), "player.db") }
}
