package com.bb.builds.screens.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_neuro
import com.bb.builds.components.BuildCard
import com.bb.builds.components.InputBranchNameDialog
import com.bb.builds.components.ResetBuildDialog
import com.bb.builds.components.ResetServerCard
import com.bb.builds.components.SignBuildDialog
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.domain.BuildData
import com.bb.builds.domain.BuildType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun CreateScreen(
    snackbarHostState: SnackbarHostState,
) {
    val viewModel = koinViewModel<CreateScreenViewModel>()
    val buildOptions = getBuildOptions()
    val fontFamily = MavenFontFamily()

    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedBuildType by remember { mutableStateOf(BuildType.MACOS) }
    var isSignDialogVisible by remember { mutableStateOf(false) }
    var isResetDialogVisible by remember { mutableStateOf(false) }
    var selectedBranchName by remember { mutableStateOf("develop") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(color = Color.White)
            .padding(all = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 4.dp),
            text = "Create Build",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            fontSize = 38.sp,
            letterSpacing = 0.1.sp,
        )

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(top = 24.dp)
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
                .padding(top = 4.dp),
            columns = GridCells.Fixed(count = 2),
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
            verticalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
            items(count = buildOptions.size) { index ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    BuildCard(
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
                    )
                }
            }
        }

        ResetServerCard(
            modifier = Modifier
                .padding(top = 12.dp),
            onResetClick = {
                isResetDialogVisible = true
            }
        )

        AnimatedVisibility(visible = isDialogVisible) {
            InputBranchNameDialog(
                onSet = { branchName ->
                    if (branchName.length < MIN_BRANCH_LENGHT) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(message = "Branch name incorrect")
                        }
                    } else {
                        isDialogVisible = false
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
                            snackbarHostState.showSnackbar(message = "Building...")
                        }
                        selectedBranchName = branchName
                    }
                },
                onDismissRequest = {
                    isDialogVisible = false
                },
                title = "Set Branch Name",
            )
        }

        AnimatedVisibility(visible = isSignDialogVisible) {
            SignBuildDialog(
                onSign = {
                    isSignDialogVisible = false
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
                        snackbarHostState.showSnackbar(message = "Server reloading...")
                        viewModel.restartServer()
                    }
                },
                onDismissRequest = {
                    isResetDialogVisible = false
                },
                title = "Do You Want To Reload Server?",
            )
        }
    }
}
