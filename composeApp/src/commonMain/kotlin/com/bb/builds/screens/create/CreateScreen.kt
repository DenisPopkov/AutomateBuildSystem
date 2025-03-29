package com.bb.builds.screens.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_neuro
import com.bb.builds.components.BuildCard
import com.bb.builds.domain.BuildType
import com.bb.builds.theme.SfFontFamily
import com.bb.builds.theme.getColorSystem
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun CreateScreen(
    snackbarHostState: SnackbarHostState,
    updateSelectedBuildType: (BuildType) -> Unit,
    showSelectBranchBottomSheet: () -> Unit,
    onOptionsSelected: (isSelected: Boolean) -> Unit,
) {
    val buildOptions = getBuildOptions()
    val sfFontFamily = SfFontFamily()
    val colors = getColorSystem()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(color = colors.white100)
            .padding(all = 16.dp)
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
                fontFamily = sfFontFamily,
                fontSize = 24.sp,
                letterSpacing = 0.2.sp,
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
                        buildData = buildOptions[index],
                        onBuildClick = {
                            updateSelectedBuildType.invoke(buildOptions[index].buildType)
                            showSelectBranchBottomSheet.invoke()
                        },
                        onOptionsClick = {
                            coroutineScope.launch {
                                if (buildOptions[index].buildType == BuildType.IOS) {
                                    snackbarHostState.showSnackbar(message = "Not supported")
                                } else {
                                    updateSelectedBuildType.invoke(buildOptions[index].buildType)
                                    showSelectBranchBottomSheet.invoke()
                                    onOptionsSelected.invoke(true)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
