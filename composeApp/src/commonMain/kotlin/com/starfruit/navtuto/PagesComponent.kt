package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.router.pages.selectNext
import com.arkivanov.decompose.router.pages.selectPrev
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

interface PagesComponent {
    val pages: Value<ChildPages<*, PageComponent>>

    fun selectPage(index: Int)
}

class DefaultPagesComponent(
    componentContext: ComponentContext,
) : PagesComponent, ComponentContext by componentContext {

    private val navigation = PagesNavigation<Config>()

    override val pages: Value<ChildPages<*, PageComponent>> =
        childPages(
            source = navigation,
            serializer = Config.serializer(), // Or null to disable navigation state saving
            initialPages = {
                Pages(
                    items = List(4) { index -> Config(data = "Item $index") },
                    selectedIndex = 0,
                )
            },
        ) { config, childComponentContext ->
            DefaultPageComponent(
                componentContext = childComponentContext,
                data = config.data,
            )
        }

    override fun selectPage(index: Int) {
        navigation.select(index = index)
    }

    @Serializable // kotlinx-serialization plugin must be applied
    private data class Config(val data: String)
}