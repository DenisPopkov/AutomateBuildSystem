package com.bb.builds.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_dots
import com.bb.builds.domain.BuildType
import com.bb.builds.isDesktop
import com.bb.builds.screens.create.BuildStateScreen
import com.bb.builds.theme.MavenFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val MOBILE_CARD_HEIGHT = 120
private const val MOBILE_CARD_WIDTH = 190

private const val DESKTOP_CARD_HEIGHT = 150
private const val DESKTOP_CARD_WIDTH = 220

@Preview
@Composable
fun BuildCard(
    buildData: BuildStateScreen.Build,
    onBuildClick: () -> Unit,
    onOptionsClick: () -> Unit,
) {
    val fontFamily = MavenFontFamily()

    val cardWidth = if (isDesktop) DESKTOP_CARD_WIDTH else MOBILE_CARD_WIDTH
    val cardHeight = if (isDesktop) DESKTOP_CARD_HEIGHT else MOBILE_CARD_HEIGHT
    val isAppleBuildType =
        buildData.buildType == BuildType.IOS || buildData.buildType == BuildType.MACOS

    Box(
        modifier = Modifier
            .size(width = cardWidth.dp, height = cardHeight.dp)
            .clip(shape = RoundedCornerShape(size = 18.dp))
            .clickable { onBuildClick.invoke() }
            .background(color = buildData.buildItemCardColor)
            .padding(all = 12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .then(
                            when {
                                isAppleBuildType -> Modifier.size(size = 30.dp)
                                buildData.buildType == BuildType.WINDOWS -> Modifier
                                    .padding(start = 4.dp, top = 4.dp)

                                else -> Modifier
                            }
                        ),
                    painter = painterResource(buildData.buildIcon),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.weight(weight = 1f))

                Box(
                    modifier = Modifier
                        .size(size = 28.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color.White.copy(alpha = 0.2f))
                        .clickable {
                            onOptionsClick.invoke()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_dots),
                        contentDescription = null,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(weight = 1f))

            Text(
                text = buildData.buildType.buildName,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamily,
                fontSize = 17.sp,
            )
        }
    }
}