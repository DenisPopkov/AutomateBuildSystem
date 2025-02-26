package com.bb.builds.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bb.builds.components.AutomateBuildDialog
import com.bb.builds.components.BBTextField
import com.bb.builds.components.BranchItem
import com.bb.builds.components.LoadingScreen
import com.bb.builds.domain.BuildData
import com.bb.builds.domain.BuildType
import com.bb.builds.screens.builds.BuildScreenViewModel
import com.bb.builds.screens.builds.BuildsScreen
import com.bb.builds.screens.create.CreateScreen
import com.bb.builds.screens.create.CreateScreenViewModel
import com.bb.builds.theme.MavenFontFamily
import com.bb.builds.theme.Theme
import com.bb.builds.theme.getColorSystem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.time.Duration.Companion.seconds

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    val navHostController = rememberNavController()
    var selectedRoute by remember { mutableStateOf<Navigation>(Navigation.Build) }
    val snackbarHostState = remember { SnackbarHostState() }

    val buildViewModel = koinViewModel<BuildScreenViewModel>()
    val createViewModel = koinViewModel<CreateScreenViewModel>()

    val coroutineScope = rememberCoroutineScope()
    val colors = getColorSystem()
    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val keyboardController = LocalSoftwareKeyboardController.current

    val isLoading by createViewModel.isLoading.collectAsState()
    val branches by createViewModel.branches.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedItemText by remember { mutableStateOf("") }
    var branchPlaceholderPlatforms by remember { mutableStateOf("") }
    var selectedBuildType by remember { mutableStateOf(BuildType.MACOS) }

    var isSignDialogVisible by remember { mutableStateOf(false) }
    var isBumpDialogVisible by remember { mutableStateOf(false) }
    var isBundleOrApkDialogVisible by remember { mutableStateOf(false) }
    var isUseDevAnalyticsDialogVisible by remember { mutableStateOf(false) }

    var isSign by remember { mutableStateOf(false) }
    var isBump by remember { mutableStateOf(false) }
    var isBundleToBuild by remember { mutableStateOf(false) }
    var isUseDevAnalytics by remember { mutableStateOf(true) }

    val filteredItems = branches.filter { it.contains(searchQuery.trim(), ignoreCase = true) }
    var isBuilding by remember { mutableStateOf(false) }

    LaunchedEffect(bottomState.currentValue) {
        if (bottomState.currentValue == ModalBottomSheetValue.Hidden) {
            keyboardController?.hide()
        }
    }

    LaunchedEffect(selectedBuildType) {
        branchPlaceholderPlatforms = when (selectedBuildType) {
            BuildType.WINDOWS -> "Windows"
            BuildType.IOS -> "iOS"
            BuildType.ANDROID -> "Android"
            BuildType.MACOS -> "macOS"
        }
    }

    LaunchedEffect(isBuilding) {
        if (isBuilding) {
            keyboardController?.hide()
            snackbarHostState.showSnackbar(message = "Building...")

            createViewModel.build(
                buildData = BuildData(
                    branchName = selectedItemText,
                    bumpVersion = isBump,
                    isBundleToBuild = isBundleToBuild,
                    sign = isSign,
                    isUseDevAnalytics = isUseDevAnalytics,
                ),
                buildType = selectedBuildType,
            )

            delay(duration = 5.seconds)

            // Clearing old states
            isBuilding = false
            isSign = false
            isBump = false
            isBundleToBuild = false
            isUseDevAnalytics = false
        }
    }

    MaterialTheme {
        ModalBottomSheetLayout(
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable {
                                    coroutineScope.launch {
                                        bottomState.hide()
                                        keyboardController?.hide()
                                        selectedItemText = ""
                                        searchQuery = ""
                                    }
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Cancel",
                                style = TextStyle(
                                    fontFamily = MavenFontFamily(),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 17.sp,
                                    lineHeight = 22.sp,
                                ),
                                color = colors.main,
                                textAlign = TextAlign.Center,
                            )
                        }

                        Spacer(modifier = Modifier.weight(weight = 1f))

                        AnimatedVisibility(
                            visible = selectedItemText.isNotEmpty(),
                            enter = fadeIn(animationSpec = spring()),
                            exit = fadeOut(animationSpec = spring())
                        ) {
                            Box(
                                modifier = Modifier
                                    .clickable {
                                        coroutineScope.launch {
                                            bottomState.hide()
                                            keyboardController?.hide()

                                            when (selectedBuildType) {
                                                BuildType.IOS -> isUseDevAnalyticsDialogVisible = true
                                                else -> isBumpDialogVisible = true
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = if (selectedBuildType == BuildType.IOS) "Build" else "Select",
                                    style = TextStyle(
                                        fontFamily = MavenFontFamily(),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 17.sp,
                                        lineHeight = 22.sp,
                                    ),
                                    color = colors.main,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }

                    BBTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Theme.spacingSystem.m),
                        onValueChange = { searchQuery = it },
                        textFieldValue = searchQuery,
                        placeholder = "Select branch for $branchPlaceholderPlatforms build"
                    )

                    if (isLoading) {
                        LoadingScreen()
                    } else {
                        LazyColumn(
                            modifier = Modifier,
                        ) {
                            itemsIndexed(items = filteredItems) { index, item ->
                                BranchItem(
                                    branch = item,
                                    branchColor = if (item == selectedItemText) colors.main else colors.black100,
                                    showDivider = filteredItems.lastIndex != index,
                                    onSelectBranchClick = { selectedItemText = it },
                                )
                            }
                        }
                    }
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
                        modifier = Modifier
                            .imePadding(),
                        hostState = snackbarHostState,
                    )
                },
            ) { paddingValues ->
                NavHost(
                    navController = navHostController,
                    startDestination = if (selectedRoute is Navigation.Build) Navigation.Build::class.simpleName
                        ?: "" else Navigation.Builds::class.simpleName ?: "",
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable(Navigation.Build::class.simpleName ?: "") {
                        CreateScreen(
                            snackbarHostState = snackbarHostState,
                            updateSelectedBuildType = { selectedBuildType = it },
                            showSelectBranchBottomSheet = { coroutineScope.launch { bottomState.show() } },
                        )

                        // for all targets, except iOS
                        AnimatedVisibility(visible = isBumpDialogVisible) {
                            AutomateBuildDialog(
                                title = "Do You Want To Bump Version?",
                                approveButtonText = "Bump",
                                cancelButtonText = "Not bump",
                                onApprove = {
                                    isBump = true
                                    isBumpDialogVisible = false

                                    when (selectedBuildType) {
                                        BuildType.ANDROID -> isBundleOrApkDialogVisible = true
                                        BuildType.MACOS -> isSignDialogVisible = true
                                        BuildType.WINDOWS -> isUseDevAnalyticsDialogVisible = true

                                        else -> {} // for iOS empty condition
                                    }
                                },
                                onDismissRequest = {
                                    isBumpDialogVisible = false
                                    isBuilding = false
                                    isSign = false
                                    isBump = false
                                    isBundleToBuild = false
                                },
                                onCancel = {
                                    isBump = false
                                    isBumpDialogVisible = false

                                    when (selectedBuildType) {
                                        BuildType.ANDROID -> isBundleOrApkDialogVisible = true
                                        BuildType.MACOS -> isSignDialogVisible = true
                                        BuildType.WINDOWS -> isUseDevAnalyticsDialogVisible = true

                                        else -> {} // for iOS empty condition
                                    }
                                },
                            )
                        }

                        // for macOS and Windows
                        AnimatedVisibility(visible = isSignDialogVisible) {
                            AutomateBuildDialog(
                                title = "Do You Want To Sign The Build?",
                                approveButtonText = "Sign",
                                cancelButtonText = "Not sign",
                                onApprove = {
                                    isSignDialogVisible = false
                                    isSign = true
                                    isUseDevAnalyticsDialogVisible = true
                                },
                                onDismissRequest = {
                                    isSignDialogVisible = false
                                    isBuilding = false
                                    isSign = false
                                    isBump = false
                                    isBundleToBuild = false
                                    isUseDevAnalytics = true
                                },
                                onCancel = {
                                    isSignDialogVisible = false
                                    isSign = false
                                    isUseDevAnalyticsDialogVisible = true
                                },
                            )
                        }

                        AnimatedVisibility(visible = isBundleOrApkDialogVisible) {
                            AutomateBuildDialog(
                                title = "Do You Want To Build APK or Bundle?",
                                approveButtonText = "APK",
                                cancelButtonText = "Bundle",
                                onApprove = {
                                    isBundleOrApkDialogVisible = false
                                    isBundleToBuild = false
                                    isUseDevAnalyticsDialogVisible = true
                                },
                                onDismissRequest = {
                                    isBundleOrApkDialogVisible = false
                                    isBuilding = false
                                    isSign = false
                                    isBump = false
                                    isBundleToBuild = false
                                },
                                onCancel = {
                                    isBundleOrApkDialogVisible = false
                                    isBundleToBuild = true
                                    isUseDevAnalyticsDialogVisible = true
                                },
                            )
                        }

                        // for all targets
                        AnimatedVisibility(visible = isUseDevAnalyticsDialogVisible) {
                            AutomateBuildDialog(
                                title = "Do You Want To Use Dev Analytics?",
                                approveButtonText = "Use Dev",
                                cancelButtonText = "Use Prod",
                                onApprove = {
                                    isUseDevAnalytics = true
                                    isBuilding = true
                                    isUseDevAnalyticsDialogVisible = false
                                },
                                onDismissRequest = {
                                    isBumpDialogVisible = false
                                    isBuilding = false
                                    isSign = false
                                    isBump = false
                                    isUseDevAnalytics = true
                                    isBundleToBuild = false
                                    isUseDevAnalyticsDialogVisible = false
                                },
                                onCancel = {
                                    isUseDevAnalytics = false
                                    isBuilding = true
                                    isUseDevAnalyticsDialogVisible = false
                                },
                            )
                        }
                    }

                    composable(Navigation.Builds::class.simpleName ?: "") {
                        BuildsScreen(
                            viewModel = buildViewModel,
                            snackbarHostState = snackbarHostState,
                        )
                    }
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