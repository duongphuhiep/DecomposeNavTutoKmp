package com.starfruit.navtuto

import AppColors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MainView(component: MainComponent) {
    Column(modifier = Modifier.background(AppColors.BgMain)) {
        Text("Main")
        Button(onClick = {component.goBack()}) {
            Text("Go Back")
        }
        for (i in 1..3) {
            Button(onClick = {component.selectItem(i)}) {
                Text("Select Detail $i")
            }
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 480, heightDp = 800)
private fun MainPreview() {
    MaterialTheme {
        MainView(mainComponentPreview)
    }
}

val mainComponentPreview = object : MainComponent{
    override fun selectItem(itemId: Int) {}
    override fun goBack() {}
}