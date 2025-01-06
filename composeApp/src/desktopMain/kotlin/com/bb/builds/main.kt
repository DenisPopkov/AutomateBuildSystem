package com.bb.builds

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_launcher
import com.bb.builds.screens.App
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Build Build!",
        state = WindowState(height = 700.dp),
        icon = painterResource(Res.drawable.ic_launcher),
    ) {
        App()
    }
}