package com.starfruit.navtuto

import AppColors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DetailsView(component: IDetailsComponent) {
    Column(
        modifier = Modifier.background(color=AppColors.BgDetails)
    ) {
        Text("Details ${component.itemId}")
        Button(onClick = {component.goBack()}) {
            Text("Go back")
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun DetailsPreview() {
    MaterialTheme {
        DetailsView(detailsComponentPreview)
    }
}


val detailsComponentPreview = object : IDetailsComponent{
    override val itemId: Int get() = 10
    override fun goBack() {}
}
