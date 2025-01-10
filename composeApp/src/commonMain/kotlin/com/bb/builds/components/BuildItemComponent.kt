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
import com.bb.builds.components.theme.MavenFontFamily
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
            .fillMaxWidth()
            .padding(start = 4.dp)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(size = 60.dp),
            painter = painterResource(Res.drawable.ic_neuro),
            contentDescription = null,
        )

        Column(
            modifier = Modifier
                .padding(start = Theme.spacingSystem.s)
                .align(alignment = Alignment.Top),
        ) {
            Text(
                text = "Neuro 3 ($platformName)",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamily,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
            )

            Text(
                text = "Version $version",
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily,
                fontSize = 12.sp,
                letterSpacing = 0.2.sp,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(width = 70.dp, height = 32.dp)
                .clip(shape = CircleShape)
                .background(color = Color.LightGray.copy(alpha = 0.5f))
                .clickable { onDownloadClick.invoke(downloadLink) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Send",
                color = Color(color = 0xFF007AFF),
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamily,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 0.1.sp,
            )
        }
    }
}
