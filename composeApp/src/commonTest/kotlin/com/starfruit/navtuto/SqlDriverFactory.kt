package com.starfruit.navtuto

import app.cash.sqldelight.db.SqlDriver

/**
 * create a SqlDriver which can run on the test runner (JVM)
 */
expect fun createTestDriver(): SqlDriver