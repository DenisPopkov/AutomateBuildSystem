package com.bb.builds.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bb.builds.theme.Theme
import com.bb.builds.theme.getColorSystem
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    val navHostController = rememberNavController()
    var selectedRoute by remember { mutableStateOf<Navigation>(Navigation.Build) }
    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()
    val colors = getColorSystem()
    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val keyboardController = LocalSoftwareKeyboardController.current

    Realm.open(
        RealmConfiguration.Builder(schema = setOf())
            .name("FUzz")
            .schemaVersion(1)
            .build()
    )

    MaterialTheme {
        ModalBottomSheetLayout(
            modifier = Modifier
                .statusBarsPadding(),
            sheetState = bottomState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colors.white100)
                        .imePadding()
                        .navigationBarsPadding()
                        .padding(all = Theme.spacingSystem.s),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 6.dp)
                            .clip(shape = CircleShape)
                            .background(color = Color.LightGray)
                            .align(alignment = Alignment.CenterHorizontally)
                            .clickable {
                                coroutineScope.launch {
                                    keyboardController?.hide()
                                    bottomState.hide()
                                }
                            }
                    )
                }
            },
        ) {
            Scaffold(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .background(color = colors.white100),
                bottomBar = {
                    BottomNavigation(
                        backgroundColor = colors.white100,
                        elevation = 0.dp,
                    ) {
                        BottomNavigationItem(
                            selected = selectedRoute is Navigation.Build,
                            onClick = {
                                navHostController.navigate(
                                    Navigation.Build::class.simpleName ?: ""
                                ) {
                                    popUpTo(Navigation.Build::class.simpleName ?: "") {
                                        inclusive = true
                                    }
                                }
                                selectedRoute = Navigation.Build
                            },
                            label = {
                                Text(
                                    text = "Create",
                                    color = colors.black100,
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Create",
                                    tint = if (selectedRoute is Navigation.Build) colors.main else Color.Gray
                                )
                            }
                        )

                        BottomNavigationItem(
                            selected = selectedRoute is Navigation.Builds,
                            onClick = {
                                navHostController.navigate(
                                    Navigation.Builds::class.simpleName ?: ""
                                ) {
                                    popUpTo(Navigation.Builds::class.simpleName ?: "") {
                                        inclusive = true
                                    }
                                }
                                selectedRoute = Navigation.Builds
                            },
                            label = {
                                Text(
                                    text = "Builds",
                                    color = colors.black100,
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.List,
                                    contentDescription = "Builds",
                                    tint = if (selectedRoute is Navigation.Builds) colors.main else Color.Gray
                                )
                            }
                        )
                    }
                },
                snackbarHost = {
                    SnackbarHost(
                        modifier = Modifier.imePadding(),
                        hostState = snackbarHostState,
                    )
                },
            ) { paddingValues ->

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