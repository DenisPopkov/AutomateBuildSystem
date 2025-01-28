package com.bb.builds.components.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
@Suppress("MagicNumber")
data class ColorSystem(
    val main: Color,
    val gray3: Color,
    val flamingo: Color,
    val flamingo30: Color,
    val white10: Color,
    val white13: Color,
    val white15: Color,
    val white20: Color,
    val white30: Color,
    val white40: Color,
    val white60: Color,
    val white75: Color,
    val white100: Color,
    val bgBars: Color,
    val bg: Color,
    val white80: Color,
    val white5: Color,
    val bgMenu: Color,
    val black5: Color,
    val black10: Color,
    val black20: Color,
    val black40: Color,
    val black70: Color,
    val black80: Color,
    val black100: Color,
    val sheet: Color,
    val settings: Color,
)

val LightColorSystem = ColorSystem(
    main = Color(0xFFFF5B20),
    gray3 = Color(0xFF828282),
    flamingo = Color(0xFFFE2C55),
    flamingo30 = Color(0x4DFE2C55),
    white10 = Color(0x1A000000),
    white13 = Color(0x21000000),
    white15 = Color(0x26000000),
    white20 = Color(0x33000000),
    white30 = Color(0x4D000000),
    white40 = Color(0x66000000),
    white60 = Color(0x99000000),
    white75 = Color(0xBF000000),
    white100 = Color(0xFF000000),
    bgBars = Color(0xFFEFEFEF),
    bg = Color(0xFFFFFFFF),
    white80 = Color(0xCC000000),
    white5 = Color(0x0D000000),
    bgMenu = Color(0xFFF5F5F5),
    black5 = Color(0x0DFFFFFF),
    black10 = Color(0x1AFFFFFF),
    black20 = Color(0x33FFFFFF),
    black40 = Color(0x66FFFFFF),
    black70 = Color(0xB3FFFFFF),
    black80 = Color(0xCCFFFFFF),
    black100 = Color(0xFFFFFFFF),
    sheet = Color(0xFFF8F8F8),
    settings = Color(0xFF007AFF),
)

val DarkColorSystem = ColorSystem(
    main = Color(0xFFFF5B20),
    gray3 = Color(0xFF828282),
    flamingo = Color(0xFFFE2C55),
    flamingo30 = Color(0x4DFE2C55),
    white10 = Color(0x1AFFFFFF),
    white13 = Color(0x21FFFFFF),
    white15 = Color(0x26FFFFFF),
    white20 = Color(0x33FFFFFF),
    white30 = Color(0x4DFFFFFF),
    white40 = Color(0x66FFFFFF),
    white60 = Color(0x99FFFFFF),
    white75 = Color(0xBFFFFFFF),
    white100 = Color(0xFFFFFFFF),
    bgBars = Color(0xCC111111),
    bg = Color(0xFF000000),
    white80 = Color(0xCCFFFFFF),
    white5 = Color(0x0DFFFFFF),
    bgMenu = Color(0xE5242424),
    black5 = Color(0x0D000000),
    black10 = Color(0x1A000000),
    black20 = Color(0x33000000),
    black40 = Color(0x66000000),
    black70 = Color(0xB3000000),
    black80 = Color(0xCC000000),
    black100 = Color(0xFF000000),
    sheet = Color(0xFF252525),
    settings = Color(0xFF007AFF),
)

@Composable
fun getColorSystem(): ColorSystem {
    return if (!isSystemInDarkTheme()) DarkColorSystem else LightColorSystem
}

