package com.bb.builds.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bb.builds.theme.getColorSystem

@Composable
fun LoadingScreen() {
    val colors = getColorSystem()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(weight = 1f))
        Box(
            modifier = Modifier
                .size(size = 40.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(size = 20.dp),
                strokeWidth = 3.dp,
                color = colors.main,
            )
        }
        Spacer(modifier = Modifier.weight(weight = 1f))
    }
}
