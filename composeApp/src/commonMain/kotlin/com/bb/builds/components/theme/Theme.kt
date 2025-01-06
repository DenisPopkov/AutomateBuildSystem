package com.bb.builds.components.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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

@Composable
fun Theme(
    mainColor: Color = Theme.colorSystem.main,
    content: @Composable () -> Unit,
) {
    val replacementColors = ColorSystem(main = mainColor)

    CompositionLocalProvider(
        LocalColorSystem provides replacementColors,
    ) {
        content()
    }
}
