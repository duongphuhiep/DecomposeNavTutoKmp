package com.starfruit.navtuto.notused

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.starfruit.navtuto.RootComponent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true, widthDp=100, heightDp=200)
private fun ConstraintPaddingExperiment(component: RootComponent) {
    MaterialTheme {
        Box(modifier = Modifier
            .background(Color.Red)
            .padding(10.dp)
            .fillMaxSize()
        ) {
            // Your content
            Column(Modifier
                .background(Color.Green)
                .clip(CircleShape)
                .padding(10.dp)
                .size(80.dp)
            ) {
                Text(
                    text = "B1",
                    modifier = Modifier
                    .background(Color.Blue)
                    .size(20.dp),
                )
                Text(
                    text = "B2",
                    modifier = Modifier
                        .background(Color.Magenta)
                        .size(20.dp),
                )
            }
        }
    }
}
/*

height(80-80) -> padding  -> size 80

*/