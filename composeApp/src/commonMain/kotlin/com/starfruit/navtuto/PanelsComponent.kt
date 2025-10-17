@file:OptIn(ExperimentalDecomposeApi::class)

package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.ChildPanels
import com.arkivanov.decompose.router.panels.ChildPanelsMode
import com.arkivanov.decompose.router.panels.Panels
import com.arkivanov.decompose.router.panels.PanelsNavigation
import com.arkivanov.decompose.router.panels.activateDetails
import com.arkivanov.decompose.router.panels.childPanels
import com.arkivanov.decompose.router.panels.dismissDetails
import com.arkivanov.decompose.router.panels.setMode
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

interface PanelsComponent {
    val panels: Value<ChildPanels<*, MainComponent, *, DetailsComponent, Nothing, Nothing>>

    fun setMode(mode: ChildPanelsMode)
}

class DefaultPanelsComponent(
    componentContext: ComponentContext,
    val onGoBack: () -> Unit,
) : PanelsComponent, ComponentContext by componentContext {

    private val nav = PanelsNavigation<Unit, DetailsConfig, Nothing>()

    @OptIn(ExperimentalSerializationApi::class)
    override val panels: Value<ChildPanels<*, MainComponent, *, DetailsComponent, Nothing, Nothing>> =
        childPanels(
            source = nav,
            serializers = Unit.serializer() to DetailsConfig.serializer(),
            initialPanels = { Panels(main = Unit) },
            handleBackButton = true,
            mainFactory = { _, ctx ->
                DefaultMainComponent(
                    componentContext = ctx,
                    onSelectItem = { nav.activateDetails(details = DetailsConfig(it)) },
                    onGoBack = onGoBack
                )
            },
            detailsFactory = { cfg, ctx ->
                DefaultDetailsComponent(
                    componentContext = ctx,
                    itemId = cfg.itemId,
                    onGoBack = nav::dismissDetails,
                )
            },
        )

    override fun setMode(mode: ChildPanelsMode) {
        nav.setMode(mode)
    }

    @Serializable
    private data class DetailsConfig(val itemId: Int)
}