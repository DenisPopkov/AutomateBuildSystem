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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_neuro
import com.bb.builds.components.BuildCard
import com.bb.builds.components.SettingsDropdownMenuContent
import com.bb.builds.components.dialogs.ResetBuildDialog
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.SfFontFamily
import com.bb.builds.components.theme.getColorSystem
import com.bb.builds.domain.BuildType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun CreateScreen(
    updateSelectedBuildType: (BuildType) -> Unit,
    showSelectBranchBottomSheet: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val buildOptions = getBuildOptions()
    val fontFamily = MavenFontFamily()
    val sfFontFamily = SfFontFamily()
    val colors = getColorSystem()

    var isResetDialogVisible by remember { mutableStateOf(false) }
    var isSettingsDropdownExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(color = colors.white100)
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
                color = colors.black100,
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
                            snackbarHostState.showSnackbar(message = "Not available now")
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
                        tint = colors.settings,
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
                color = colors.black100,
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
                            if (buildType == BuildType.WINDOWS) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = "Not available now")
                                }
                            } else {
                                updateSelectedBuildType.invoke(buildOptions[index].buildType)
                                showSelectBranchBottomSheet.invoke()
                            }
                        },
                        onOptionsClick = {
//                            isResetDialogVisible = true
                        }
                    )
                }
            }
        }

        AnimatedVisibility(visible = isResetDialogVisible) {
            ResetBuildDialog(
                onReset = {
//                    isResetDialogVisible = false

                    coroutineScope.launch {
//                        viewModel.stopBuild(selectedBuildType)
//                        snackbarHostState.showSnackbar(message = "Build stopping...")
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
