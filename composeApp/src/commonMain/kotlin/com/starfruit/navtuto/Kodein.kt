package com.starfruit.navtuto

import org.kodein.di.*
import org.kodein.di.conf.ConfigurableDI

/**
 * This function is usually called once in the application's entry to init the app dependencies.
 * The [contextModule] is for
 * - Providing more dependencies from the application's entry (for eg. ApplicationContext)
 * - Overriding some dependencies with Mock implementations in tests.
 */
fun ConfigurableDI.initAppDependencies(contextModule: DI.Module): ConfigurableDI {
    this.addImport(commonModule)
    this.addImport(platformModule)
    this.addImport(contextModule)
    return this
}

/**
 * All the dependencies in commonMain are declared here.
 */
private val commonModule = DI.Module("common") {
    bindSingleton<AlertDialogComponent.Factory> { DefaultAlertDialogComponent.Factory() }
    bindSingleton<DetailsComponent.Factory> { DefaultDetailsComponent.Factory() }
    bindSingleton<ItemComponent.Factory> { DefaultItemComponent.Factory() }
    bindSingleton<ListComponent.Factory> { DefaultListComponent.Factory(instance()) }
    bindSingleton<MainComponent.Factory> { DefaultMainComponent.Factory() }
    bindSingleton<PageComponent.Factory> { DefaultPageComponent.Factory() }
    bindSingleton<PagesComponent.Factory> { DefaultPagesComponent.Factory(instance()) }
    bindSingleton<PanelsComponent.Factory> {
        DefaultPanelsComponent.Factory(
            instance(),
            instance()
        )
    }
    bindSingleton<RootComponent.Factory> {
        DefaultRootComponent.Factory(
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }
    bindSingleton<ScreenAComponent.Factory> { DefaultScreenAComponent.Factory(instance()) }
    bindSingleton<ScreenBComponent.Factory> { DefaultScreenBComponent.Factory() }
}

