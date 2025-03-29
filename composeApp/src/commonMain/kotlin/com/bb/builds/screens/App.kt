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

    var isUseDevAnalyticsDialogVisible by remember { mutableStateOf(false) }
    var isUseForDevPurpose by remember { mutableStateOf(true) }

    var isSelectedFromOptions by remember { mutableStateOf(false) }
    var showRebuildDSPDialog by remember { mutableStateOf(false) }

    val filteredItems = branches.filter { it.contains(searchQuery.trim(), ignoreCase = true) }
    var isBuilding by remember { mutableStateOf(false) }

    LaunchedEffect(bottomState.currentValue) {
        if (bottomState.currentValue == ModalBottomSheetValue.Hidden) {
            keyboardController?.hide()
        }
    }

    LaunchedEffect(selectedBuildType) {
        branchPlaceholderPlatforms = when (selectedBuildType) {
            BuildType.WINDOWS -> if (isSelectedFromOptions) "Windows DSP" else "Windows"
            BuildType.IOS -> "iOS"
            BuildType.ANDROID -> if (isSelectedFromOptions) "Android DSP" else "Android"
            BuildType.MACOS -> if (isSelectedFromOptions) "MacOS DSP" else "MacOS"
        }
    }

    LaunchedEffect(isBuilding) {
        if (isBuilding) {
            keyboardController?.hide()
            snackbarHostState.showSnackbar(message = "Building...")

            createViewModel.build(
                buildData = BuildData(
                    branchName = selectedItemText,
                    isUseDevAnalytics = isUseForDevPurpose,
                ),
                buildType = selectedBuildType,
            )

            delay(duration = 5.seconds)

            // Clearing old states
            isBuilding = false
            isUseForDevPurpose = false
        }
    }

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
                                        isSelectedFromOptions = false
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
                                            isUseDevAnalyticsDialogVisible = !isSelectedFromOptions
                                            showRebuildDSPDialog = isSelectedFromOptions
                                        }
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "Select",
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
                            showSelectBranchBottomSheet = {
                                coroutineScope.launch { bottomState.show() }
                            },
                            onOptionsSelected = { isSelectedFromOptions = it }
                        )

                        AnimatedVisibility(visible = isUseDevAnalyticsDialogVisible && !isSelectedFromOptions) {
                            AutomateBuildDialog(
                                title = "Choose Build Purpose",
                                approveButtonText = "Dev",
                                cancelButtonText = "Prod",
                                onApprove = {
                                    isUseForDevPurpose = true
                                    isBuilding = true
                                    isUseDevAnalyticsDialogVisible = false
                                },
                                onDismissRequest = {
                                    isBuilding = false
                                    isUseForDevPurpose = true
                                    isUseDevAnalyticsDialogVisible = false
                                },
                                onCancel = {
                                    isUseForDevPurpose = false
                                    isBuilding = true
                                    isUseDevAnalyticsDialogVisible = false
                                },
                            )
                        }

                        AnimatedVisibility(visible = showRebuildDSPDialog) {
                            AutomateBuildDialog(
                                title = "Rebuild DSP library",
                                approveButtonText = "Rebuild",
                                cancelButtonText = "Cancel",
                                onApprove = {
                                    when (selectedBuildType) {
                                        BuildType.ANDROID -> {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(message = "Rebuilding...")
                                            }

                                            coroutineScope.launch {
                                                createViewModel.rebuildAndroidDSPLibrary(selectedItemText)
                                            }
                                        }
                                        BuildType.MACOS, BuildType.WINDOWS -> {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(message = "Rebuilding...")
                                            }

                                            coroutineScope.launch {
                                                createViewModel.rebuildDSPLibrary(selectedItemText)
                                            }
                                        }
                                        else -> {}
                                    }

                                    showRebuildDSPDialog = false
                                },
                                onDismissRequest = {
                                    showRebuildDSPDialog = false
                                    isSelectedFromOptions = false
                                },
                                onCancel = {
                                    showRebuildDSPDialog = false
                                    isSelectedFromOptions = false
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