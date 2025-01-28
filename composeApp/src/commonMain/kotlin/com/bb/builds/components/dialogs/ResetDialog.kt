package com.bb.builds.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.Theme
import com.bb.builds.components.theme.getColorSystem

@Composable
fun ResetBuildDialog(
    title: String,
    description: String,
    onDismissRequest: () -> Unit,
    onReset: () -> Unit
) {
    ResetDialog(
        title = title,
        description = description,
        onDismissRequest = onDismissRequest,
        onReset = onReset,
    )
}

@Composable
fun ResetDialog(
    title: String,
    description: String,
    onDismissRequest: () -> Unit,
    onReset: () -> Unit
) {
    val colors = getColorSystem()

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(width = 290.dp)
                .background(
                    color = colors.white100,
                    shape = RoundedCornerShape(size = 14.dp)
                )
                .clip(shape = RoundedCornerShape(size = 14.dp))
        ) {
            Spacer(modifier = Modifier.height(height = Theme.spacingSystem.s))

            Text(
                text = title,
                style = TextStyle(
                    fontFamily = MavenFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                ),
                color = colors.black100,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Theme.spacingSystem.s)
            )
            Spacer(modifier = Modifier.height(height = Theme.spacingSystem.s))

            Text(
                text = description,
                style = TextStyle(
                    fontFamily = MavenFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                color = colors.black100,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Theme.spacingSystem.s)
            )
            Spacer(modifier = Modifier.height(height = Theme.spacingSystem.s))

            Divider(
                thickness = 1.dp,
                color = Color(color = 0x1A000000)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Theme.spacingSystem.xl)
            ) {
                Box(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxHeight()
                        .clickable(onClick = onDismissRequest),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cancel",
                        style = TextStyle(
                            fontFamily = MavenFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 17.sp,
                            lineHeight = 22.sp,
                            letterSpacing = (-0.41).sp
                        ),
                        color = colors.main,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(width = 1.dp)
                        .background(color = colors.black10)
                )
                Box(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxHeight()
                        .clickable(onClick = onReset),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Stop",
                        style = TextStyle(
                            fontFamily = MavenFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 17.sp,
                            lineHeight = 22.sp,
                            letterSpacing = (-0.41).sp
                        ),
                        color = colors.main,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
