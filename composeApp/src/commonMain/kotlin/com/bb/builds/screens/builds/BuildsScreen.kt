package com.bb.builds.screens.builds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_sparkle
import com.bb.builds.components.BuildItemComponent
import com.bb.builds.components.MavenFontFamily
import com.bb.builds.components.NavMenu
import com.bb.builds.components.theme.Theme
import com.bb.builds.domain.BuildItem
import org.jetbrains.compose.resources.painterResource

@Composable
fun BuildsScreen(
    onCreateScreen: () -> Unit,
) {
    val fontFamily = MavenFontFamily()

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
            fontSize = 40.sp,
            letterSpacing = 0.2.sp,
        )

        if (builds.isEmpty()) {
            Spacer(modifier = Modifier.weight(weight = 1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NoBuilds()
            }

            Spacer(modifier = Modifier.weight(weight = 1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(top = Theme.spacingSystem.l)
            ) {
                items(builds) {
                    BuildItemComponent(
                        version = it.version,
                        platformName = it.platformName,
                        downloadLink = it.buildLink,
                        onDownloadClick = {},
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(weight = 1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(weight = 1f))
            NavMenu(
                selectedDefaultItem = "Builds",
                onCreateScreen = onCreateScreen,
                onBuildsScreen = {},
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
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

private val builds = listOf(
    BuildItem(
        version = "3.5.8 (294)",
        platformName = "Android",
        buildLink = "",
    ),
    BuildItem(
        version = "3.5.8 (296)",
        platformName = "macOS",
        buildLink = "",
    ),
)