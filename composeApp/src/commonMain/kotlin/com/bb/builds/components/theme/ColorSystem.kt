package com.bb.builds.components.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
@Suppress("MagicNumber")
data class ColorSystem(
    val main: Color = Color(0xFFFF5B20),
    val gray3: Color = Color(0xFF828282),
    val flamingo: Color = Color(0xFFFE2C55),
    val flamingo30: Color = Color(0x4DFE2C55),
    val white10: Color = Color(0x1AFFFFFF),
    val white13: Color = Color(0x21FFFFFF),
    val white15: Color = Color(0x26FFFFFF),
    val white20: Color = Color(0x33FFFFFF),
    val white30: Color = Color(0x4DFFFFFF),
    val white40: Color = Color(0x66FFFFFF),
    val white60: Color = Color(0x99FFFFFF),
    val white75: Color = Color(0xBFFFFFFF),
    val white100: Color = Color(0xFFFFFFFF),
    val bgBars: Color = Color(0xCC111111),
    val bg: Color = Color(0xFF000000),
    val white80: Color = Color(0xCCFFFFFF),
    val white5: Color = Color(0x0DFFFFFF),
    val bgMenu: Color = Color(0xE5242424),
    val black5: Color = Color(0x0D000000),
    val black10: Color = Color(0x1A000000),
    val black20: Color = Color(0x33000000),
    val black40: Color = Color(0x66000000),
    val black70: Color = Color(0xB3000000),
    val black80: Color = Color(0xCC000000),
    val sheet: Color = Color(0xFF252525),
)
