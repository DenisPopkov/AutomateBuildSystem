package com.bb.builds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.Theme
import com.bb.builds.isDesktop
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val MOBILE_CARD_HEIGHT = 120
private const val MOBILE_CARD_WIDTH = 380

private const val DESKTOP_CARD_HEIGHT = 150
private const val DESKTOP_CARD_WIDTH = 440

@Preview
@Composable
fun ResetServerCard(
    modifier: Modifier,
    onResetClick: () -> Unit = {},
) {
    val fontFamily = MavenFontFamily()

    val cardWidth = if (isDesktop) DESKTOP_CARD_WIDTH else MOBILE_CARD_WIDTH
    val cardHeight = if (isDesktop) DESKTOP_CARD_HEIGHT else MOBILE_CARD_HEIGHT

    Box(
        modifier = modifier
            .size(width = cardWidth.dp, height = cardHeight.dp)
            .clip(shape = RoundedCornerShape(size = 18.dp))
            .clickable { onResetClick.invoke() }
            .background(color = Theme.colorSystem.main.copy(alpha = 0.5f))
            .padding(all = 12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Icon(
                modifier = Modifier
                    .size(size = 30.dp),
                imageVector = Icons.Filled.Settings,
                contentDescription = null,
                tint = Color.White,
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Text(
                text = "Restart server",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamily,
                fontSize = 17.sp,
            )
        }
    }
}