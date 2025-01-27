package com.bb.builds.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Icon
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_clear_medium
import com.bb.builds.components.FilterableDropdownMenu
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.Theme
import org.jetbrains.compose.resources.painterResource

@Composable
fun InputBranchNameDialog(
    buildButtonText: String,
    branches: List<String>,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var textFieldValue by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(width = 290.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(size = 14.dp),
                )
                .clip(shape = RoundedCornerShape(size = 14.dp))
        ) {
            Spacer(modifier = Modifier.height(height = 8.dp))

            var selectedIndex by remember { mutableStateOf(value = -1) }
            FilterableDropdownMenu(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                label = "Choose Branch",
                items = branches,
                onItemSelected = { index, item ->
                    selectedIndex = index
                    textFieldValue = item
                },
                selectedIndex = selectedIndex,
            )

            Spacer(modifier = Modifier.height(height = 12.dp))

            Divider(thickness = 1.dp, color = Color(0x1A000000))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 48.dp),
            ) {
                Box(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxHeight()
                        .clickable(onClick = onDismissRequest),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Cancel",
                        style = TextStyle(
                            fontFamily = MavenFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 17.sp,
                            lineHeight = 22.sp,
                        ),
                        color = Theme.colorSystem.main,
                        textAlign = TextAlign.Center,
                    )
                }

                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(width = 1.dp),
                    color = Color(0x1A000000)
                )

                Box(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxHeight()
                        .clickable { onConfirm(textFieldValue) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = buildButtonText,
                        style = TextStyle(
                            fontFamily = MavenFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 17.sp,
                            lineHeight = 22.sp,
                        ),
                        color = Theme.colorSystem.main,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun BBTextField(
    modifier: Modifier = Modifier,
    selectedValue: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    focusRequester: FocusRequester = FocusRequester(),
    onValueChange: (String) -> Unit,
    onFocusChanged: (state: FocusState) -> Unit = {},
) {
    var textFieldValue by remember { mutableStateOf(selectedValue) }
    val customTextSelectionColors = TextSelectionColors(
        handleColor = Theme.colorSystem.main,
        backgroundColor = Theme.colorSystem.main.copy(alpha = 0.2f)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        Column(
            modifier = modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 44.dp)
                    .background(
                        color = Theme.colorSystem.black10,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .padding(start = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 36.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged(onFocusChanged),
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = keyboardType,
                        imeAction = imeAction
                    ),
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                        onValueChange(it)
                    },
                    singleLine = true,
                    visualTransformation = visualTransformation,
                    textStyle = TextStyle(
                        fontFamily = MavenFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 22.2.sp,
                        letterSpacing = (-0.5).sp,
                        color = Theme.colorSystem.black70,
                        textAlign = TextAlign.Start
                    ),
                    cursorBrush = SolidColor(Theme.colorSystem.main),
                )

                if (textFieldValue.isNotEmpty()) {
                    Icon(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterEnd)
                            .clip(shape = CircleShape)
                            .clickable(
                                onClick = {
                                    textFieldValue = ""
                                    onValueChange.invoke(textFieldValue)
                                },
                            )
                            .padding(all = 8.dp),
                        painter = painterResource(Res.drawable.ic_clear_medium),
                        contentDescription = null,
                        tint = Theme.colorSystem.black70,
                    )
                }
            }
        }
    }
}
