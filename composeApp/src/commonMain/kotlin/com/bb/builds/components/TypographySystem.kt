package com.bb.builds.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import automatebuildsystem.composeapp.generated.resources.*
import automatebuildsystem.composeapp.generated.resources.MavenPro_Black
import automatebuildsystem.composeapp.generated.resources.MavenPro_Bold
import automatebuildsystem.composeapp.generated.resources.MavenPro_Regular
import automatebuildsystem.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun MavenFontFamily() = FontFamily(
    Font(Res.font.MavenPro_Black, weight = FontWeight.Black),
    Font(Res.font.MavenPro_Bold, weight = FontWeight.Bold),
    Font(Res.font.MavenPro_Regular, weight = FontWeight.Normal),
    Font(Res.font.MavenPro_Medium, weight = FontWeight.Medium),
    Font(Res.font.MavenPro_Semibold, weight = FontWeight.SemiBold),
)
