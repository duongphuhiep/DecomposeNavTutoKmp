package com.starfruit.navtuto

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.starfruit.navtuto.data.PlayerDb
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import java.util.Properties
import kotlin.coroutines.CoroutineContext

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val platformModule = DI.Module("jvm") {
    bindSingleton<CoroutineContext> { Dispatchers.IO }
    bindSingleton<SqlDriver> {
        JdbcSqliteDriver("jdbc:sqlite:player.db", Properties(), PlayerDb.Schema.synchronous()) }
}