package com.starfruit.navtuto

import com.starfruit.navtuto.data.PlayerDb
import com.starfruit.navtuto.data.PlayerDb.Companion.invoke
import kotlinx.coroutines.Dispatchers
import org.kodein.di.*
import org.kodein.di.conf.ConfigurableDI
import kotlin.coroutines.CoroutineContext

/**
 * This function is usually called once in the application (or tests) entry to
 * initialize the app dependencies graph.
 *
 * @param contextModule
 * - Providing more dependencies from the application's entry or test entry (for eg. ApplicationContext)
 * - Overriding some dependencies with Mock implementations in tests.
 */
fun ConfigurableDI.initAppDependencies(contextModule: DI.Module): ConfigurableDI {
    this.addImport(commonModule)
    this.addImport(platformModule)
    this.addImport(contextModule, allowOverride = true)
    return this
}

/**
 * All the dependencies in commonMain are declared here.
 */
private val commonModule = DI.Module("common") {

    //local sqlite database
    bindSingleton<PlayerDb> { PlayerDb(instance()) }

    bindSingleton { AlertDialogComponent.Factory() }
    bindSingleton { DetailsComponent.Factory() }
    bindSingleton { ItemComponent.Factory() }
    bindSingleton { ListComponent.Factory(instance()) }
    bindSingleton { MainComponent.Factory() }
    bindSingleton { PageComponent.Factory() }
    bindSingleton { PagesComponent.Factory(instance()) }
    bindSingleton {
        PanelsComponent.Factory(
            instance(),
            instance()
        )
    }
    bindSingleton {
        RootComponent.Factory(
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }
    bindSingleton { ScreenAComponent.Factory(instance()) }
    bindSingleton { ScreenBComponent.Factory(instance(), instance("IO")) }
    bindSingleton { WaiterModalComponent.Factory() }

}

