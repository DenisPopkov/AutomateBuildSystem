package com.bb.builds

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.bb.builds.di.initKoin
import com.bb.builds.screens.App
import java.io.File
import java.io.PrintStream

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Build Build!",
            state = WindowState(height = 700.dp),
        ) {
            initKoin()
            App()
        }
    }

    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        throwable.printStackTrace()
        try {
            File("C:/Users/Public/NeuroDesktop/java_crash_full.txt").printWriter().use { out ->
                out.println("Uncaught exception in thread: ${thread.name}")
                throwable.printStackTrace(out)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    System.setProperty("skiko.data.path", "C:/Users/Public/NeuroDesktop/AppData/Local")
    System.setOut(PrintStream(File("C:/Users/Public/NeuroDesktop/java_stdout.txt")))
    System.setErr(PrintStream(File("C:/Users/Public/NeuroDesktop/java_stderr.txt")))
}
