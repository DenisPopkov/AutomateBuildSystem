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
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_sparkle
import com.bb.builds.components.BuildItemComponent
import com.bb.builds.components.LoadingScreen
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.SfFontFamily
import com.bb.builds.components.theme.getColorSystem
import com.bb.builds.domain.BuildId
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun BuildsScreen(
    viewModel: BuildScreenViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val builds by viewModel.builds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sfFontFamily = SfFontFamily()
    val coroutineScope = rememberCoroutineScope()

    val colors = getColorSystem()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(color = colors.white100)
            .padding(all = 16.dp)
    ) {
        if (isLoading) {
            LoadingScreen()
        }

        Text(
            modifier = Modifier
                .padding(start = 4.dp),
            text = "Builds",
            color = colors.black100,
            fontWeight = FontWeight.Bold,
            fontFamily = sfFontFamily,
            fontSize = 32.sp,
            letterSpacing = 0.2.sp,
        )

        if (builds.isEmpty()) {
            Spacer(modifier = Modifier.weight(weight = 1f))

            if (!isLoading) {
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
                    .padding(top = 20.dp),
            ) {
                items(builds) {
                    BuildItemComponent(
                        version = it.version,
                        platformName = it.platformName,
                        onSendClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message = "Sending...")
                                viewModel.send(BuildId(it.id))
                            }
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
    val colors = getColorSystem()

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
            color = colors.black100,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            fontSize = 24.sp,
            letterSpacing = 0.2.sp,
        )
    }
}
