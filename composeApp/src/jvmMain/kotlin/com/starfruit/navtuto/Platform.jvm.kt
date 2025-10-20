package com.starfruit.navtuto

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.starfruit.navtuto.data.PlayerDb
import org.kodein.di.DI
import org.kodein.di.bindProvider
import java.util.Properties

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val platformModule = DI.Module("jvm") {
    bindProvider<SqlDriver> { JdbcSqliteDriver("jdbc:sqlite:player.db", Properties(), PlayerDb.Schema) }
}