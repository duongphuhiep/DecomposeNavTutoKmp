package com.starfruit.navtuto

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PageView(component: PageComponent) {
    Text("Page: ${component.data}")
}