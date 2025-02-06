package com.bb.builds.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Suppress("CompositionLocalAllowlist")
val LocalColorSystem = staticCompositionLocalOf {
    ColorSystem(
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
}

@Suppress("CompositionLocalAllowlist")
val LocalSpacingSystem = staticCompositionLocalOf {
    SpacingSystem()
}

object Theme {
    val colorSystem: ColorSystem
        @Composable
        get() = LocalColorSystem.current
    val spacingSystem: SpacingSystem
        @Composable
        get() = LocalSpacingSystem.current
}
