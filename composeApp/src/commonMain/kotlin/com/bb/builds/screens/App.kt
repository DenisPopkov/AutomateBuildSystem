package com.bb.builds.screens

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bb.builds.screens.create.CreateScreen
import com.bb.builds.screens.builds.BuildsScreen
import kotlinx.serialization.Serializable

@Composable
fun App() {
    val navHostController = rememberNavController()
    val selectedRoute by remember { mutableStateOf<Navigation>(Navigation.Build) }

    MaterialTheme {
        NavHost(
            navController = navHostController,
            startDestination = selectedRoute,
        ) {
            composable<Navigation.Build> {
                CreateScreen(
                    onBuildsScreen = {
                        navHostController.navigate(Navigation.Builds)
                    }
                )
            }

            composable<Navigation.Builds> {
                BuildsScreen(
                    onCreateScreen = {
                        navHostController.navigate(Navigation.Build)
                    }
                )
            }
        }
    }
}

sealed interface Navigation {

    @Serializable
    data object Build : Navigation

    @Serializable
    data object Builds : Navigation
}