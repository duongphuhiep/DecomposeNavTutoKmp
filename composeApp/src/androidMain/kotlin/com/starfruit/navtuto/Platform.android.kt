package com.starfruit.navtuto

import android.os.Build
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.starfruit.navtuto.data.PlayerDb
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.provider

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val platformModule: DI.Module = DI.Module("android") {
    bindProvider<SqlDriver> { AndroidSqliteDriver(PlayerDb.Schema, instance(), "player.db") }
}

