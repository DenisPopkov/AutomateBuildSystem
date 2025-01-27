package com.bb.builds.screens.create

import androidx.compose.ui.graphics.Color
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_android
import automatebuildsystem.composeapp.generated.resources.ic_ios
import automatebuildsystem.composeapp.generated.resources.ic_macos
import automatebuildsystem.composeapp.generated.resources.ic_windows
import com.bb.builds.domain.BuildType

internal fun getBuildOptions() = listOf(
    BuildStateScreen.Build(
        buildItemId = 0,
        buildType = BuildType.ANDROID,
        buildIcon = Res.drawable.ic_android,
        buildItemCardColor = Color(color = 0xFF32BE71),
    ),
    BuildStateScreen.Build(
        buildItemId = 1,
        buildType = BuildType.IOS,
        buildIcon = Res.drawable.ic_ios,
        buildItemCardColor = Color(color = 0xFF007CFF),
    ),
    BuildStateScreen.Build(
        buildItemId = 2,
        buildType = BuildType.MACOS,
        buildIcon = Res.drawable.ic_macos,
        buildItemCardColor = Color(color = 0xFFB9B9B9),
    ),
    BuildStateScreen.Build(
        buildItemId = 3,
        buildType = BuildType.WINDOWS,
        buildIcon = Res.drawable.ic_windows,
        buildItemCardColor = Color(color = 0xFFFF7D60),
    ),
)
