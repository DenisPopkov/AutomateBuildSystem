package com.bb.builds.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.getColorSystem

@Composable
fun SettingsDropdownMenuContent(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onThemeChangeClick: () -> Unit,
) {
    val fontFamily = MavenFontFamily()
    val shapes = Shapes(
        small = RoundedCornerShape(size = 4.dp),
        medium = RoundedCornerShape(size = 12.dp),
        large = RoundedCornerShape(size = 0.dp),
    )
    val colors = getColorSystem()

    MaterialTheme(shapes = shapes) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .background(color = colors.white100)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.LightGray
                    ),
                    shape = RoundedCornerShape(size = 12.dp),
                )
        ) {
            DropdownMenuItem(onClick = onThemeChangeClick) {
                Text(
                    text = "Change theme",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = colors.black100,
                )
            }
        }
    }
}

@Composable
fun OptionsDropdownMenuContent(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onStopBuildClick: () -> Unit,
    onOpenLogsClick: () -> Unit,
) {
    val fontFamily = MavenFontFamily()
    val shapes = Shapes(
        small = RoundedCornerShape(size = 4.dp),
        medium = RoundedCornerShape(size = 12.dp),
        large = RoundedCornerShape(size = 0.dp),
    )
    val colors = getColorSystem()

    MaterialTheme(
        shapes = shapes,
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .background(color = colors.white100)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.LightGray
                    ),
                    shape = RoundedCornerShape(size = 12.dp),
                )
        ) {
            DropdownMenuItem(onClick = onStopBuildClick) {
                Text(
                    text = "Stop build",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = colors.black100,
                )
            }

            Divider(thickness = 1.dp, color = colors.black20)

            DropdownMenuItem(onClick = onOpenLogsClick) {
                Text(
                    text = "Open logs",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = colors.black100,
                )
            }
        }
    }
}

