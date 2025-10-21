package com.starfruit.navtuto

import app.cash.sqldelight.db.SqlDriver
import com.starfruit.navtuto.data.HockeyPlayer
import com.starfruit.navtuto.data.PlayerDb
import com.starfruit.navtuto.data.PlayerQueries
import org.kodein.di.DI
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.instance
import kotlin.test.Test

class SqlDelightTest {
    fun doDatabaseThings(driver: SqlDriver) {
        val database = PlayerDb(driver)
        val playerQueries: PlayerQueries = database.playerQueries

        println(playerQueries.selectAll().executeAsList())
        // [HockeyPlayer(15, "Ryan Getzlaf")]

        playerQueries.insert(player_number = 10, full_name = "Corey Perry")
        println(playerQueries.selectAll().executeAsList())
        // [HockeyPlayer(15, "Ryan Getzlaf"), HockeyPlayer(10, "Corey Perry")]

        val player = HockeyPlayer(10, "Ronald McDonald")
        playerQueries.insertFullPlayerObject(player)
    }
    val di = ConfigurableDI().initAppDependencies(
        DI.Module("contextAndMock") {}
    )

    @Test
    fun selectTest() {
        val driver by di.instance<SqlDriver>()
        val database = PlayerDb(driver)
        val playerQueries: PlayerQueries = database.playerQueries
        println(playerQueries.selectAll().executeAsList())
    }
}