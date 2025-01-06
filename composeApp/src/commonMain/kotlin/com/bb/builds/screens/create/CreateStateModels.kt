package com.bb.builds.screens.create

import androidx.compose.ui.graphics.Color
import com.bb.builds.domain.BuildType
import org.jetbrains.compose.resources.DrawableResource

sealed class BuildStateScreen {
  data class Build(
    val buildItemId: Int,
    val buildType: BuildType,
    val buildIcon: DrawableResource,
    val buildItemCardColor: Color,
  ) : BuildStateScreen()
}
