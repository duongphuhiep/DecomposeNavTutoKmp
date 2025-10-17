package com.starfruit.navtuto

import com.arkivanov.decompose.ComponentContext

interface PageComponent {
    val data: String
}

class DefaultPageComponent(
    componentContext: ComponentContext,
    override val data: String,
) : PageComponent, ComponentContext by componentContext