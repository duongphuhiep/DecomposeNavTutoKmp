package com.starfruit.navtuto

import android.os.Build
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.starfruit.navtuto.data.PlayerDb
import kotlinx.coroutines.Dispatchers
import org.kodein.di.*
import kotlin.coroutines.CoroutineContext

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual val platformModule: DI.Module = DI.Module("android") {
    bindSingleton<CoroutineContext> { Dispatchers.IO }
    bindSingleton<SqlDriver> {
        AndroidSqliteDriver(
            schema = PlayerDb.Schema.synchronous(),
            context = instance(),
            name = "player.db"
        )
    }
}

