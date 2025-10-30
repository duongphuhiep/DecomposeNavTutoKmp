@file:OptIn(ExperimentalDecomposeApi::class)

package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

interface IPanelsComponent {
    val panels: Value<ChildPanels<*, IMainComponent, *, IDetailsComponent, Nothing, Nothing>>

    fun setMode(mode: ChildPanelsMode)
}

class PanelsComponent(
    componentContext: ComponentContext,
    private val mainComponentFactory: MainComponent.Factory,
    private val detailsComponentFactory: DetailsComponent.Factory,
    val onGoBack: () -> Unit,
) : ComponentContext by componentContext, IPanelsComponent {
    class Factory(
        private val mainComponentFactory: MainComponent.Factory,
        private val detailsComponentFactory: DetailsComponent.Factory,
    ) {
        operator fun invoke(
            componentContext: ComponentContext,
            onGoBack: () -> Unit,
        ): PanelsComponent = PanelsComponent(
            componentContext = componentContext,
            mainComponentFactory = mainComponentFactory,
            detailsComponentFactory = detailsComponentFactory,
            onGoBack = onGoBack,
        )
    }

    private val nav = PanelsNavigation<Unit, DetailsConfig, Nothing>()

    @OptIn(ExperimentalSerializationApi::class)
    override val panels: Value<ChildPanels<*, IMainComponent, *, IDetailsComponent, Nothing, Nothing>> =
        childPanels(
            source = nav,
            serializers = Unit.serializer() to DetailsConfig.serializer(),
            initialPanels = { Panels(main = Unit) },
            handleBackButton = true,
            mainFactory = { _, ctx ->
                mainComponentFactory(
                    componentContext = ctx,
                    onSelectItem = { nav.activateDetails(details = DetailsConfig(it)) },
                    onGoBack = onGoBack
                )
            },
            detailsFactory = { cfg, ctx ->
                detailsComponentFactory(
                    componentContext = ctx,
                    itemId = cfg.itemId,
                    onGoBack = nav::dismissDetails,
                )
            },
        )

    override fun setMode(mode: ChildPanelsMode) = nav.setMode(mode)

    @Serializable
    private data class DetailsConfig(val itemId: Int)
}
