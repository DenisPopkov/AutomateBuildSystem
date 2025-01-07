package com.bb.builds.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_neuro
import com.bb.builds.components.theme.Theme
import com.bb.builds.domain.BuildType
import org.jetbrains.compose.resources.painterResource

@Composable
fun BuildItemComponent(
    version: String = "3.5.8 (294)",
    platformName: String = BuildType.ANDROID.buildName,
    downloadLink: String = "",
    onDownloadClick: (link: String) -> Unit,
) {
    val fontFamily = MavenFontFamily()

    Row(
        modifier = Modifier
            .padding(start = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier
                .size(size = 70.dp),
            painter = painterResource(Res.drawable.ic_neuro),
            contentDescription = null,
        )

        Column(
            modifier = Modifier
                .padding(start = Theme.spacingSystem.l),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "Source Audio Neuro 3",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
            )

            Text(
                text = "Version $version",
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
            )

            Text(
                text = platformName,
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
            )
        }

        Spacer(modifier = Modifier.weight(weight = 1f))

        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 36.dp)
                .clip(shape = CircleShape)
                .background(color = Color.LightGray.copy(alpha = 0.5f))
                .clickable { onDownloadClick.invoke(downloadLink) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier
                    .padding(all = Theme.spacingSystem.xxxs),
                text = "Install",
                color = Color.Blue.copy(alpha = 0.7f),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
            )
        }
    }
}