package com.bb.builds

import androidx.compose.ui.window.ComposeUIViewController
import com.bb.builds.di.initKoin
import com.bb.builds.screens.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
