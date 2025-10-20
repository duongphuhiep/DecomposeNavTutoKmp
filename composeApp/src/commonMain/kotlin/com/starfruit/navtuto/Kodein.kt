package com.starfruit.navtuto

import org.kodein.di.DI
import org.kodein.di.conf.global

fun initGlobalDi(contextModule: DI.Module) {
    DI.global.addImport(contextModule)
    DI.global.addImport(platformModule)
    DI.global.addImport(commonModule)
}

private val commonModule = DI.Module("common") {

}