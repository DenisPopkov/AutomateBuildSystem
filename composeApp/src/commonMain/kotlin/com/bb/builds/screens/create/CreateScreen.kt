package com.bb.builds.screens.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_neuro
import com.bb.builds.components.BuildCard
import com.bb.builds.components.OptionsDropdownMenuContent
import com.bb.builds.components.SettingsDropdownMenuContent
import com.bb.builds.components.dialogs.InputBranchNameDialog
import com.bb.builds.components.dialogs.ResetBuildDialog
import com.bb.builds.components.dialogs.SignBuildDialog
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.SfFontFamily
import com.bb.builds.domain.BuildData
import com.bb.builds.domain.BuildType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun CreateScreen(
    viewModel: CreateScreenViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val buildOptions = getBuildOptions()
    val fontFamily = MavenFontFamily()
    val sfFontFamily = SfFontFamily()

    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedBuildType by remember { mutableStateOf(BuildType.MACOS) }
    var isSignDialogVisible by remember { mutableStateOf(false) }
    var isResetDialogVisible by remember { mutableStateOf(false) }
    var selectedBranchName by remember { mutableStateOf("") }
    var isSettingsDropdownExpanded by remember { mutableStateOf(false) }

    val branches by viewModel.branches.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(color = Color.White)
            .padding(all = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = "Create Build",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = sfFontFamily,
                fontSize = 32.sp,
                letterSpacing = 0.2.sp,
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Box {
                SettingsDropdownMenuContent(
                    expanded = isSettingsDropdownExpanded,
                    onDismissRequest = { isSettingsDropdownExpanded = false },
                    onThemeChangeClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Not available now")
                        }
                    },
                )

                Box(
                    modifier = Modifier
                        .size(size = 30.dp)
                        .clip(shape = CircleShape)
                        .clickable { isSettingsDropdownExpanded = true },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier
                            .size(size = 26.dp),
                        imageVector = Icons.Filled.Settings,
                        tint = Color(0xFF007AFF),
                        contentDescription = null,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(top = 20.dp)
                .padding(start = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(size = 36.dp),
                painter = painterResource(Res.drawable.ic_neuro),
                contentDescription = null,
            )

            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = "Neuro 3",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                fontSize = 24.sp,
                letterSpacing = 0.3.sp,
            )
        }

        LazyVerticalGrid(
            modifier = Modifier
                .padding(top = 4.dp)
                .width(width = 400.dp),
            columns = GridCells.Fixed(count = 2),
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
            verticalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
            items(count = buildOptions.size) { index ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                ) {
                    BuildCard(
                        snackbarHostState = snackbarHostState,
                        buildData = buildOptions[index],
                        onBuildClick = {
                            val buildType = buildOptions[index].buildType
                            if (buildType == BuildType.WINDOWS || buildType == BuildType.IOS) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = "Not available yet")
                                }
                            } else {
                                selectedBuildType = buildOptions[index].buildType
                                isDialogVisible = true
                            }
                        },
                        onOptionsClick = {
                            isResetDialogVisible = true
                        }
                    )
                }
            }
        }

        AnimatedVisibility(visible = isDialogVisible) {
            val isMobile =
                selectedBuildType == BuildType.ANDROID || selectedBuildType == BuildType.IOS
            InputBranchNameDialog(
                buildButtonText = if (isMobile) "Build" else "Select",
                onConfirm = { branchName ->
                    isDialogVisible = false
                    selectedBranchName = branchName
                    if (selectedBuildType == BuildType.ANDROID) {
                        viewModel.build(
                            buildData = BuildData(
                                branchName = selectedBranchName,
                                sign = true,
                            ),
                            buildType = selectedBuildType,
                        )
                    } else {
                        isSignDialogVisible = true
                    }
                    coroutineScope.launch {
                        if (isMobile)
                            snackbarHostState.showSnackbar(message = "Building...")
                    }
                },
                branches = branches,
                onDismissRequest = {
                    isDialogVisible = false
                },
            )
        }

        AnimatedVisibility(visible = isSignDialogVisible) {
            SignBuildDialog(
                onSign = {
                    isSignDialogVisible = false
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message = "Building...")
                    }
                    viewModel.build(
                        buildData = BuildData(
                            branchName = selectedBranchName,
                            sign = true,
                        ),
                        buildType = selectedBuildType,
                    )
                },
                onDismissRequest = {
                    isSignDialogVisible = false
                    viewModel.build(
                        buildData = BuildData(
                            branchName = selectedBranchName,
                            sign = false,
                        ),
                        buildType = selectedBuildType,
                    )
                },
                title = "Do You Want To Sign The Build?",
            )
        }

        AnimatedVisibility(visible = isResetDialogVisible) {
            ResetBuildDialog(
                onReset = {
                    isResetDialogVisible = false

                    coroutineScope.launch {
                        viewModel.stopBuild(selectedBuildType)
                        snackbarHostState.showSnackbar(message = "Build stopping...")
                    }
                },
                onDismissRequest = {
                    isResetDialogVisible = false
                },
                title = "Do You Want To Stop Build?",
                description = "This build will be stopped. Afterward, you can restart it."
            )
        }
    }
}
