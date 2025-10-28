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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
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
import com.bb.builds.components.AutomateBuildDialog
import com.bb.builds.components.BBTextField
import com.bb.builds.components.BranchItem
import com.bb.builds.components.LoadingScreen
import com.bb.builds.domain.BuildData
import com.bb.builds.domain.BuildType
import com.bb.builds.domain.UploadTarget
import com.bb.builds.screens.create.CreateScreen
import com.bb.builds.screens.create.CreateScreenViewModel
import com.bb.builds.theme.MavenFontFamily
import com.bb.builds.theme.Theme
import com.bb.builds.theme.getColorSystem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.time.Duration.Companion.seconds

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    val snackbarHostState = remember { SnackbarHostState() }

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
    var showWindowsOptionsDialog by remember { mutableStateOf(false) }
    var showRebuildJARDialog by remember { mutableStateOf(false) }

    var showArchitectureDialog by remember { mutableStateOf(false) }
    var showDSPArchitectureDialog by remember { mutableStateOf(false) }
    var isMacBuildX86 by remember { mutableStateOf(false) }


    var showUploadTargetDialog by remember { mutableStateOf(false) }
    var selectedUploadTarget by remember { mutableStateOf(UploadTarget.SLACK) }

    val filteredItems = branches.filter { it.contains(searchQuery.trim(), ignoreCase = true) }
    var isBuilding by remember { mutableStateOf(false) }

    LaunchedEffect(bottomState.currentValue) {
        if (bottomState.currentValue == ModalBottomSheetValue.Hidden) {
            keyboardController?.hide()
        }
    }

    LaunchedEffect(selectedBuildType) {
        branchPlaceholderPlatforms = when (selectedBuildType) {
            BuildType.WINDOWS -> if (isSelectedFromOptions) "Windows" else "Windows"
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
                    uploadTarget = selectedUploadTarget.targetName,
                ),
                buildType = selectedBuildType,
                isX86 = isMacBuildX86,
            )

            delay(duration = 5.seconds)

            isBuilding = false
            isUseForDevPurpose = false
        }
    }

    MaterialTheme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { paddingValues ->
            ModalBottomSheetLayout(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(paddingValues),
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
                                modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        bottomState.hide()
                                        keyboardController?.hide()

                                        if (isSelectedFromOptions) {
                                            if (selectedBuildType == BuildType.WINDOWS) {
                                                showWindowsOptionsDialog = true
                                            } else {
                                                showRebuildDSPDialog = true
                                            }
                                        } else {
                                            if (selectedBuildType == BuildType.MACOS) {
                                                showArchitectureDialog = true
                                            } else {
                                                isUseDevAnalyticsDialogVisible = true
                                            }
                                        }
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
                        LazyColumn {
                            itemsIndexed(filteredItems) { index, item ->
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
            CreateScreen(
                snackbarHostState = snackbarHostState,
                updateSelectedBuildType = { selectedBuildType = it },
                showSelectBranchBottomSheet = {
                    coroutineScope.launch { bottomState.show() }
                },
                onOptionsSelected = { isSelectedFromOptions = it }
            )

            AnimatedVisibility(visible = isUseDevAnalyticsDialogVisible) {
                AutomateBuildDialog(
                    title = "Choose Build Purpose",
                    approveButtonText = if (selectedBuildType == BuildType.WINDOWS) "Test" else "Dev",
                    cancelButtonText = if (selectedBuildType == BuildType.WINDOWS) "Release" else "Prod",
                    onApprove = {
                        isUseForDevPurpose = true
                        if (selectedBuildType == BuildType.ANDROID) {
                            showUploadTargetDialog = true
                            isUseDevAnalyticsDialogVisible = false
                        } else {
                            isBuilding = true
                            isUseDevAnalyticsDialogVisible = false
                        }
                    },
                    onCancel = {
                        isUseForDevPurpose = false
                        isBuilding = true
                        isUseDevAnalyticsDialogVisible = false
                    },
                    onDismissRequest = {
                        isBuilding = false
                        isUseForDevPurpose = true
                        isUseDevAnalyticsDialogVisible = false
                    }
                )
            }

            AnimatedVisibility(visible = showArchitectureDialog) {
                AutomateBuildDialog(
                    title = "Select MacOS Architecture",
                    approveButtonText = "x86",
                    cancelButtonText = "ARM",
                    onApprove = {
                        isMacBuildX86 = true
                        showArchitectureDialog = false
                        isUseDevAnalyticsDialogVisible = true
                    },
                    onCancel = {
                        isMacBuildX86 = false
                        showArchitectureDialog = false
                        isUseDevAnalyticsDialogVisible = true
                    },
                    onDismissRequest = {
                        isBuilding = false
                        showArchitectureDialog = false
                    }
                )
            }

            AnimatedVisibility(visible = showUploadTargetDialog) {
                AutomateBuildDialog(
                    title = "Select Upload Target",
                    approveButtonText = "Slack",
                    cancelButtonText = "Firebase",
                    onApprove = {
                        selectedUploadTarget = UploadTarget.SLACK
                        showUploadTargetDialog = false
                        isBuilding = true
                    },
                    onCancel = {
                        selectedUploadTarget = UploadTarget.FIREBASE
                        showUploadTargetDialog = false
                        isBuilding = true
                    },
                    onDismissRequest = {
                        showUploadTargetDialog = false
                    }
                )
            }

            AnimatedVisibility(visible = showRebuildDSPDialog) {
                AutomateBuildDialog(
                    title = "Rebuild DSP Library",
                    approveButtonText = "Rebuild",
                    cancelButtonText = "Cancel",
                    onApprove = {
                        when (selectedBuildType) {
                            BuildType.ANDROID -> {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Rebuilding...")
                                    createViewModel.rebuildAndroidDSPLibrary(
                                        branchName = selectedItemText,
                                        isUseDevAnalytics = false
                                    )
                                }
                            }

                            BuildType.WINDOWS -> {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Rebuilding...")
                                    createViewModel.rebuildDSPLibrary(
                                        branchName = selectedItemText,
                                        isWindows = true,
                                        isX86 = false,
                                        isUseDevAnalytics = false
                                    )
                                }
                            }

                            BuildType.MACOS -> {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Rebuilding DSP for both x86 and ARM (M1)...")
                                    createViewModel.rebuildMacDSPBothArchitectures(
                                        branchName = selectedItemText,
                                        isUseDevAnalytics = false
                                    )
                                }
                            }

                            else -> {}
                        }

                        showRebuildDSPDialog = false
                        isSelectedFromOptions = false
                    },
                    onCancel = {
                        showRebuildDSPDialog = false
                        isSelectedFromOptions = false
                    },
                    onDismissRequest = {
                        showRebuildDSPDialog = false
                        isSelectedFromOptions = false
                    }
                )
            }

            AnimatedVisibility(visible = showDSPArchitectureDialog) {
                AutomateBuildDialog(
                    title = "Select MacOS DSP Architecture",
                    approveButtonText = "x86",
                    cancelButtonText = "ARM",
                    onApprove = {
                        isMacBuildX86 = true
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Rebuilding...")
                            createViewModel.rebuildDSPLibrary(
                                branchName = selectedItemText,
                                isWindows = false,
                                isX86 = true,
                                isUseDevAnalytics = false
                            )
                        }
                        showDSPArchitectureDialog = false
                    },
                    onCancel = {
                        isMacBuildX86 = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Rebuilding...")
                            createViewModel.rebuildDSPLibrary(
                                branchName = selectedItemText,
                                isWindows = false,
                                isX86 = false,
                                isUseDevAnalytics = false
                            )
                        }
                        showDSPArchitectureDialog = false
                    },
                    onDismissRequest = {
                        showDSPArchitectureDialog = false
                    }
                )
            }
            AnimatedVisibility(visible = showWindowsOptionsDialog) {
                AutomateBuildDialog(
                    title = "Windows Options",
                    approveButtonText = "DSP",
                    cancelButtonText = "JAR",
                    onApprove = {
                        showWindowsOptionsDialog = false
                        showRebuildDSPDialog = true
                    },
                    onCancel = {
                        showWindowsOptionsDialog = false
                        showRebuildJARDialog = true
                    },
                    onDismissRequest = {
                        showWindowsOptionsDialog = false
                    }
                )
            }

            AnimatedVisibility(visible = showRebuildJARDialog) {
                AutomateBuildDialog(
                    title = "Rebuild JAR Update",
                    approveButtonText = "Build",
                    cancelButtonText = "Cancel",
                    onApprove = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Rebuilding JAR...")
                            createViewModel.rebuildJARLibrary(
                                branchName = selectedItemText
                            )
                        }
                        showRebuildJARDialog = false
                        isSelectedFromOptions = false
                    },
                    onCancel = {
                        showRebuildJARDialog = false
                        isSelectedFromOptions = false
                    },
                    onDismissRequest = {
                        showRebuildJARDialog = false
                        isSelectedFromOptions = false
                    }
                )
            }
        }
        }
    }
}