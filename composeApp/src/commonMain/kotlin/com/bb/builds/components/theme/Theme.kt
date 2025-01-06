package com.bb.builds.components.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

@Suppress("CompositionLocalAllowlist")
val LocalColorSystem = staticCompositionLocalOf {
    ColorSystem()
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
