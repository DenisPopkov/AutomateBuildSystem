package com.bb.builds

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.bb.builds.di.initKoin
import com.bb.builds.screens.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Build Build!",
        state = WindowState(height = 700.dp),
    ) {
        initKoin()
        App()
    }
}