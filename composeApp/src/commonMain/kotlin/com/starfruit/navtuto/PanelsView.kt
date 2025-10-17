@file:OptIn(ExperimentalDecomposeApi::class)

package com.starfruit.navtuto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.panels.ChildPanels
import com.arkivanov.decompose.extensions.compose.experimental.panels.ChildPanelsAnimators
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.*
import com.arkivanov.decompose.router.panels.ChildPanels
import com.arkivanov.decompose.router.panels.ChildPanelsMode
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.starfruit.util.compose.ChildPanelsModeChangedEffect
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PanelsView(component: PanelsComponent) {
    ChildPanelsModeChangedEffect(component::setMode)
    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Panels")
    }
    ChildPanels(
        panels = component.panels,
        mainChild = { MainView(it.instance) },
        detailsChild = { DetailsView(it.instance) },
        animators = ChildPanelsAnimators(single = fade() + scale(), dual = fade() to fade()),
//        predictiveBackParams = { // See the docs below
//            PredictiveBackParams(
//                backHandler = component.backHandler,
//                onBack = component::onBackClicked,
//                animatable = ::materialPredictiveBackAnimatable,
//            )
//        },
    )
}

@Composable
@Preview(showBackground = true)
private fun PanelsPreview() {
    MaterialTheme {
        PanelsView(panelsComponentPreview1)
    }
}

val panelsComponentPreview1 = object : PanelsComponent {
    override val panels: Value<ChildPanels<*, MainComponent, *, DetailsComponent, Nothing, Nothing>>
        get() = MutableValue(
            ChildPanels(
                main = Child.Created("", mainComponentPreview),
                details = Child.Created("", detailsComponentPreview),
                mode = ChildPanelsMode.DUAL
            )
        )
    override fun setMode(mode: ChildPanelsMode) {}
}
//val panelsComponentPreview2 = DefaultPanelsComponent(rootComponentPreview)