package com.starfruit.util.compose

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.ChildPanelsMode
import com.arkivanov.decompose.router.panels.ChildPanelsMode.*

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun ChildPanelsModeChangedEffect(onModeChanged: (ChildPanelsMode) -> Unit) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val mode = if (windowSizeClass.isWidthAtLeastBreakpoint(840)) DUAL else SINGLE

    DisposableEffect(mode) {
        onModeChanged(mode)
        onDispose {}
    }
}