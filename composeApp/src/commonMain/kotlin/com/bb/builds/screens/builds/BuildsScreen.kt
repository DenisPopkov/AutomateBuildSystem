package com.bb.builds.screens.builds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_sparkle
import com.bb.builds.components.BuildItemComponent
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.Theme
import com.bb.builds.domain.BuildId
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.time.Duration.Companion.seconds

@OptIn(KoinExperimentalAPI::class)
@Composable
fun BuildsScreen() {
    val viewModel = koinViewModel<BuildScreenViewModel>()
    val builds by viewModel.builds.collectAsState()
    val fontFamily = MavenFontFamily()
    var showNoBuilds by remember { mutableStateOf(false) }

    LaunchedEffect(builds.isEmpty()) {
        if (builds.isEmpty()) {
            delay(duration = 2.seconds)
            showNoBuilds = true
        } else {
            showNoBuilds = false
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(color = Color.White)
            .padding(all = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 4.dp),
            text = "Builds",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            fontSize = 38.sp,
            letterSpacing = 0.2.sp,
        )

        if (builds.isEmpty()) {
            Spacer(modifier = Modifier.weight(weight = 1f))

            if (showNoBuilds) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NoBuilds()
                }
            }

            Spacer(modifier = Modifier.weight(weight = 1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(top = Theme.spacingSystem.m),
            ) {
                items(builds) {
                    BuildItemComponent(
                        version = it.version,
                        platformName = it.platformName,
                        onSendClick = {
                            viewModel.send(BuildId(it.id))
                        },
                    )
                }
            }
        }
    }
}


@Composable
fun NoBuilds() {
    val fontFamily = MavenFontFamily()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier
                .size(size = 56.dp),
            painter = painterResource(Res.drawable.ic_sparkle),
            contentDescription = null,
        )

        Text(
            modifier = Modifier
                .padding(top = 12.dp),
            text = "No Builds Yet",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            fontSize = 24.sp,
            letterSpacing = 0.2.sp,
        )
    }
}
