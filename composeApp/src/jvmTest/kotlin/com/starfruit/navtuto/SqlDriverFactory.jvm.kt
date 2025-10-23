package com.starfruit.navtuto

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.starfruit.navtuto.data.PlayerDb
import org.kodein.di.bindSingleton
import java.util.Properties

actual fun createTestDriver(): SqlDriver {
      return  JdbcSqliteDriver("jdbc:sqlite:player.db", Properties(), PlayerDb.Schema.synchronous())
//    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
//    PlayerDb.Schema.create(driver)
//    return driver
}