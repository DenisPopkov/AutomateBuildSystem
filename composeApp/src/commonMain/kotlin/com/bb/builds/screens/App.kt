package com.bb.builds.screens

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bb.builds.components.theme.Theme
import com.bb.builds.screens.builds.BuildsScreen
import com.bb.builds.screens.create.CreateScreen
import kotlinx.serialization.Serializable

@Composable
fun App() {
    val navHostController = rememberNavController()
    var selectedRoute by remember { mutableStateOf<Navigation>(Navigation.Build) }
    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackbarHostState)

    MaterialTheme {
        Scaffold(
            modifier = Modifier
                .navigationBarsPadding(),
            bottomBar = {
                BottomNavigation(
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                ) {
                    BottomNavigationItem(
                        selected = selectedRoute is Navigation.Build,
                        onClick = {
                            navHostController.navigate(Navigation.Build::class.simpleName ?: "") {
                                popUpTo(Navigation.Build::class.simpleName ?: "") {
                                    inclusive = true
                                }
                            }
                            selectedRoute = Navigation.Build
                        },
                        label = { Text("Create") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Create",
                                tint = if (selectedRoute is Navigation.Build) Theme.colorSystem.main else Color.Gray
                            )
                        }
                    )
                    BottomNavigationItem(
                        selected = selectedRoute is Navigation.Builds,
                        onClick = {
                            navHostController.navigate(Navigation.Builds::class.simpleName ?: "") {
                                popUpTo(Navigation.Builds::class.simpleName ?: "") {
                                    inclusive = true
                                }
                            }
                            selectedRoute = Navigation.Builds
                        },
                        label = { Text("Builds") },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Builds",
                                tint = if (selectedRoute is Navigation.Builds) Theme.colorSystem.main else Color.Gray
                            )
                        }
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(
                    modifier = Modifier
                        .imePadding(),
                    hostState = snackbarHostState,
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navHostController,
                startDestination = if (selectedRoute is Navigation.Build) Navigation.Build::class.simpleName
                    ?: "" else Navigation.Builds::class.simpleName ?: "",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Navigation.Build::class.simpleName ?: "") {
                    CreateScreen(snackbarHostState = snackbarHostState)
                }

                composable(Navigation.Builds::class.simpleName ?: "") {
                    BuildsScreen(
                        snackbarHostState = snackbarHostState,
                    )
                }
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