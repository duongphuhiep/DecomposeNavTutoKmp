package com.starfruit.navtuto

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.starfruit.navtuto.data.PlayerDb
import com.starfruit.navtuto.data.PlayerQueries
import com.starfruit.util.formatUtcIso
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.*
import org.kodein.di.*
import org.kodein.di.conf.ConfigurableDI
import kotlin.test.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SqlDelightTest {
    val di = ConfigurableDI().initAppDependencies(
        DI.Module("contextAndMock") {
            //replace the real sql driver with a test driver
            bindSingleton(overrides = true) { createTestDriver() }
        }
    )

    @Test
    fun selectTest() = runTest {
        val database by di.instance<PlayerDb>()
        val playerQueries: PlayerQueries = database.playerQueries
        println(playerQueries.selectAllPlayer().executeAsList())
    }

    @Test
    fun kvTest() = runTest {
        val database by di.instance<PlayerDb>()
        val queries = database.kvQueries

        //key not exist => null value
        assertNull(queries.get("key not exist").awaitAsOneOrNull());
        assertEquals(1, queries.upsert("test", "testValue"))

        val v1 = queries.get("test").awaitAsOneOrNull()
        println(v1)
        assertEquals("testValue", v1?.value_)

        assertEquals(1, queries.upsert("test", "testValue2"))
        val v2 = queries.get("test").awaitAsOneOrNull()
        println(v2)
        assertEquals("testValue2", v2?.value_)

        queries.removeAll()
        assertNull(queries.get("test").awaitAsOneOrNull());
    }

    @Test
    fun kvCacheTest() = runTest {
        val database by di.instance<PlayerDb>()
        val queries = database.kvCacheQueries
        val now = Clock.System.now()
        val past1 = now.minus(1, DateTimeUnit.HOUR, TimeZone.UTC)
        val past2 = now.minus(2, DateTimeUnit.HOUR, TimeZone.UTC)

        //key not exist => null value
        assertNull(queries.get("key not exist").awaitAsOneOrNull())

        //create an expired entry
        assertEquals(1, queries.upsert("test", "testValue", past1.formatUtcIso()))

        //get expired entry
        val kv1 = queries.get("test").awaitAsOneOrNull()
        assertEquals("testValue", kv1?.value_)
        assertEquals(past1.formatUtcIso(), kv1?.expiryAt)

        //update the entry with new value and expired time
        assertEquals(1, queries.upsert("test", "testValue2", past2.formatUtcIso()))

        //get updated entry
        val kv2 = queries.peek("test").awaitAsOneOrNull()
        assertEquals("testValue2", kv2?.value_)
        assertEquals(past2.formatUtcIso(), kv2?.expiryAt)

        //add 2 never-expired entries
        assertEquals(1, queries.upsert("neverExpiredKey1", "neverExpiredValue", null))
        assertEquals(1, queries.upsert("neverExpiredKey2", "neverExpiredValue", ""))

        //remove all expired entries
        assertEquals(1, queries.removeExpired())

        //left 2 never-expired entries
        assertEquals(2, queries.peekAll().awaitAsList().size)

        //remove all entries which have not been access since past2
        assertEquals(0, queries.removeUnused(past1.formatUtcIso()))

        //remove 1 entry
        assertEquals(1, queries.remove("neverExpiredKey1"))

        //left 1 never-expired entry
        assertEquals(1, queries.peekAll().awaitAsList().size)

        //remove all entries
        assertEquals(1, queries.removeAll())

        //nothing left
        assertEquals(0, queries.peekAll().awaitAsList().size)
    }
}