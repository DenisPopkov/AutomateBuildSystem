package com.bb.builds.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bb.builds.components.theme.MavenFontFamily

@Composable
fun SettingsDropdownMenuContent(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onThemeChangeClick: () -> Unit,
) {
    val fontFamily = MavenFontFamily()

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(onClick = onThemeChangeClick) {
            Text(
                text = "Change Theme",
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
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

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(onClick = onStopBuildClick) {
            Text(
                text = "Stop Running Build",
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
        }

        DropdownMenuItem(onClick = onOpenLogsClick) {
            Text(
                text = "Open Build Logs",
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
        }
    }
}
