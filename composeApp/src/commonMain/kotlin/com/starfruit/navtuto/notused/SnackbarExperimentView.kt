package com.starfruit.navtuto.notused

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.starfruit.navtuto.RootComponent
import kotlinx.coroutines.launch

@Composable
fun SnackbarExperimentView(component: RootComponent) {
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme {
        Box(modifier = Modifier.safeContentPadding().fillMaxSize()) {
            // Your content
            Column {
                // Place SnackbarHost wherever you want
                SnackbarHost(
                    hostState = snackbarHostState,
                )

                val scope = rememberCoroutineScope()
                Button(onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Hello without Scaffold!")
                    }
                }) {
                    Text("Show Snackbar")
                }
            }
        }
    }
}