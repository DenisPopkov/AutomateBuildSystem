package com.bb.builds.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import automatebuildsystem.composeapp.generated.resources.ic_android
import automatebuildsystem.composeapp.generated.resources.ic_macos
import automatebuildsystem.composeapp.generated.resources.ic_windows
import com.bb.builds.theme.MavenFontFamily
import com.bb.builds.theme.Theme
import com.bb.builds.theme.getColorSystem
import com.bb.builds.domain.BuildType
import org.jetbrains.compose.resources.painterResource

@Composable
fun BuildItemComponent(
    version: String = "3.5.8 (294)",
    date: String = "30.01.24",
    platformName: String = BuildType.ANDROID.buildName,
    onSendClick: () -> Unit,
) {
    val colors = getColorSystem()
    val fontFamily = MavenFontFamily()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (platformName) {
            BuildType.ANDROID.buildName -> {
                Box(
                    modifier = Modifier
                        .size(size = 60.dp)
                        .clip(shape = RoundedCornerShape(size = 10.dp))
                        .background(color = Color(0xFF32BE71)),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier
                            .size(size = 40.dp),
                        painter = painterResource(Res.drawable.ic_android),
                        contentDescription = null,
                    )
                }
            }

            BuildType.MACOS.buildName -> {
                Box(
                    modifier = Modifier
                        .size(size = 60.dp)
                        .clip(shape = RoundedCornerShape(size = 10.dp))
                        .background(color = Color(0xFFB9B9B9)),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier
                            .size(size = 38.dp),
                        painter = painterResource(Res.drawable.ic_macos),
                        contentDescription = null,
                    )
                }
            }

            BuildType.WINDOWS.buildName -> {
                Box(
                    modifier = Modifier
                        .size(size = 60.dp)
                        .clip(shape = RoundedCornerShape(size = 10.dp))
                        .background(color = Color(0xFFFF7D60)),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier
                            .size(size = 32.dp),
                        painter = painterResource(Res.drawable.ic_windows),
                        contentDescription = null,
                    )
                }
            }

            else ->  {

            }
        }

        Column(
            modifier = Modifier
                .padding(start = Theme.spacingSystem.s)
                .align(alignment = Alignment.Top),
        ) {
            Text(
                text = "$platformName $version",
                color = colors.black100,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamily,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
            )

            Text(
                text = "by $date",
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
                .background(color = Color.LightGray.copy(alpha = if (isSystemInDarkTheme()) 0.2f else 0.5f))
                .clickable { onSendClick.invoke() },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Send",
                color = colors.settings.copy(alpha = 0.9f),
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamily,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 0.1.sp,
            )
        }
    }

}
