package com.bb.builds.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_clear_medium
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.Theme
import org.jetbrains.compose.resources.painterResource

@Composable
fun FieldDialog(
    title: String,
    fieldValue: TextFieldValue,
    onFieldConfirm: () -> Unit,
    onFieldValueChanged: (value: TextFieldValue) -> Unit,
    onDismissRequest: () -> Unit,
    buttons: List<DialogButtonInfo>,
    label: String? = null,
    trailingLabel: String? = null,
    trailingImagePainter: Painter? = null,
    onTrailingImageClick: () -> Unit = {},
) {
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
                .clip(shape = RoundedCornerShape(size = 14.dp)),
        ) {
            Spacer(modifier = Modifier.height(height = Theme.spacingSystem.s))
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = MavenFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                ),
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Theme.spacingSystem.s),
            )
            Spacer(modifier = Modifier.height(Theme.spacingSystem.xxxs))
            Spacer(modifier = Modifier.height(Theme.spacingSystem.s))

            val focusRequester = remember { FocusRequester() }
            BBTextField(
                modifier = Modifier
                    .padding(horizontal = Theme.spacingSystem.s),
                value = fieldValue.text,
                placeholder = "",
                onValueChange = onFieldValueChanged,
                trailingImagePainter = trailingImagePainter,
                onTrailingImageClick = onTrailingImageClick,
                imeAction = ImeAction.Done,
                keyboardAction = { onFieldConfirm() },
                trailingLabel = trailingLabel,
                label = label,
                focusRequester = focusRequester,
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            Spacer(modifier = Modifier.height(Theme.spacingSystem.s))
            MainSeparator(height = 1.dp)
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(Theme.spacingSystem.xl),
            ) {
                buttons.forEachIndexed { index, info ->
                    Box(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .height(height = Theme.spacingSystem.xl)
                            .clickable { info.onClick.invoke() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = info.name,
                            style = info.style,
                            color = info.color ?: Theme.colorSystem.main,
                            textAlign = TextAlign.Center,
                        )
                    }
                    if (index < buttons.size - 1) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width = 1.dp)
                                .background(color = Theme.colorSystem.black10),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputBranchNameDialog(
    title: String,
    name: String,
    onDismissRequest: () -> Unit,
    onSet: (String) -> Unit,
) {
    var fieldValue by remember(name) {
        mutableStateOf(
            TextFieldValue(
                text = name,
                selection = TextRange(name.length),
            ),
        )
    }

    val onChange: (TextFieldValue) -> Unit = { textFieldValue ->
        val filteredText = textFieldValue.text.lowercase()
        fieldValue = textFieldValue.copy(
            text = filteredText,
            selection = TextRange(filteredText.length)
        )
    }

    FieldDialog(
        title = title,
        fieldValue = fieldValue,
        onFieldConfirm = { },
        onFieldValueChanged = onChange,
        onDismissRequest = onDismissRequest,
        buttons = listOf(
            DialogButtonInfo(
                name = "Cancel",
                onClick = onDismissRequest,
                style = TextStyle(
                    fontFamily = MavenFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    letterSpacing = (-0.41).sp,
                ),
                color = Theme.colorSystem.constMain,
            ),
            DialogButtonInfo(
                name = "Build",
                onClick = {
                    onSet.invoke(fieldValue.text)
                },
                style = TextStyle(
                    fontFamily = MavenFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    letterSpacing = (-0.41).sp,
                ),
                color = Theme.colorSystem.constMain,
                enabled = true,
            ),
        ),
        trailingImagePainter = if (fieldValue.text.isNotEmpty()) {
            painterResource(Res.drawable.ic_clear_medium)
        } else {
            null
        },
        onTrailingImageClick = {
            fieldValue = TextFieldValue()
        },
    )
}

@Composable
fun BBTextField(
    value: String,
    placeholder: String,
    onValueChange: (value: TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    label: String? = null,
    trailingLabel: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingImagePainter: Painter? = null,
    leadingImagePainter: Painter? = null,
    onLeadingImageClick: () -> Unit = {},
    onTrailingImageClick: () -> Unit = {},
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    focusRequester: FocusRequester = FocusRequester(),
    keyboardAction: KeyboardActionScope.() -> Unit = {},
    onFocusChanged: (state: FocusState) -> Unit = {},
) {
    var hasFocus by remember { mutableStateOf(value = false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            label?.let {
                Text(
                    text = label,
                    style = TextStyle(
                        fontFamily = MavenFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                    ),
                    color = Theme.colorSystem.black70,
                    modifier = Modifier.weight(weight = 1f),
                    textAlign = TextAlign.Start,
                )
            }
            if (trailingLabel != null && hasFocus) {
                Text(
                    text = trailingLabel,
                    style = TextStyle(
                        fontFamily = MavenFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                    ),
                    color = Theme.colorSystem.black70,
                )
            }
        }

        if (label != null || trailingLabel != null) {
            Spacer(Modifier.height(height = Theme.spacingSystem.xxs))
        }

        Box {
            BasicTextField(
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = keyboardType,
                    imeAction = imeAction,
                ),
                keyboardActions = KeyboardActions(keyboardAction),
                value = TextFieldValue(
                    text = value,
                    selection = TextRange(value.length)
                ),
                readOnly = !enabled,
                onValueChange = { textFieldValue ->
                    onValueChange(
                        textFieldValue.copy(
                            selection = TextRange(textFieldValue.text.length)
                        )
                    )
                },
                enabled = true,
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                textStyle = TextStyle(
                    fontFamily = MavenFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 22.2.sp,
                    letterSpacing = (-0.5).sp,
                ).copy(
                    color = Theme.colorSystem.black70,
                ),
                cursorBrush = SolidColor(Theme.colorSystem.main),
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { focusState ->
                        onFocusChanged(focusState)
                        hasFocus = focusState.hasFocus
                    },
            ) { innerTextField ->
                RawField(
                    currentValue = value,
                    placeholder = placeholder,
                    innerTextField = innerTextField,
                    singleLine = singleLine,
                    trailingImagePainter = trailingImagePainter,
                    onTrailingImageClick = onTrailingImageClick,
                    leadingImagePainter = leadingImagePainter,
                    onLeadingImageClick = onLeadingImageClick,
                )
            }
        }
    }
}

@Composable
fun MainSeparator(
    modifier: Modifier = Modifier,
    height: Dp = 1.dp,
    color: Color = Theme.colorSystem.black5,
) = Divider(modifier = modifier, thickness = height, color = color)

@Suppress("CognitiveComplexMethod")
@Composable
private fun RawField(
    currentValue: String,
    placeholder: String,
    singleLine: Boolean,
    trailingImagePainter: Painter?,
    onTrailingImageClick: () -> Unit,
    leadingImagePainter: Painter?,
    onLeadingImageClick: () -> Unit,
    innerTextField: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                other = if (singleLine) {
                    Modifier.height(height = Theme.spacingSystem.xl)
                } else {
                    Modifier
                },
            )
            .background(
                color = Theme.colorSystem.black10,
                shape = RoundedCornerShape(Theme.spacingSystem.xxs),
            )
            .padding(
                start = if (leadingImagePainter == null) {
                    Theme.spacingSystem.s
                } else {
                    Theme.spacingSystem.xxs
                },
            )
            .run { if (singleLine) this else padding(end = Theme.spacingSystem.s) },
        contentAlignment = Alignment.CenterStart,
    ) {
        if (currentValue.isEmpty()) {
            Text(
                modifier = Modifier
                    .padding(
                        start = if (leadingImagePainter == null) {
                            0.dp
                        } else {
                            Theme.spacingSystem.l
                        },
                    ),
                text = placeholder,
                style = TextStyle(
                    fontFamily = MavenFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 22.2.sp,
                    letterSpacing = (-0.5).sp,
                ),
                color = Color.Black,
            )
        }

        Row(
            modifier = Modifier
                .padding(),
        ) {
            leadingImagePainter?.let {
                TrailingImage(
                    painter = it,
                    onImageClick = onLeadingImageClick,
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.CenterStart)
                        .size(size = Theme.spacingSystem.l)
                        .align(alignment = Alignment.CenterVertically),
                )
            }
            Row(
                modifier = Modifier
                    .padding(
                        top = 10.5.dp,
                        bottom = 10.5.dp,
                        end = Theme.spacingSystem.xxs,
                    )
                    .weight(weight = 1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                innerTextField()
            }
            trailingImagePainter?.let {
                TrailingImage(
                    painter = it,
                    onImageClick = onTrailingImageClick,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(end = Theme.spacingSystem.xxs)
                        .size(size = Theme.spacingSystem.m),
                )
            }
        }
    }
}

@Composable
private fun TrailingImage(
    painter: Painter,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Image(
    modifier = modifier
        .clip(shape = CircleShape)
        .clickable(onClick = onImageClick, enabled = true),
    painter = painter,
    contentDescription = null,
    colorFilter = ColorFilter.tint(color = Theme.colorSystem.black70),
)

data class DialogButtonInfo(
    val name: String,
    val onClick: () -> Unit,
    val style: TextStyle,
    val color: Color? = null,
    val enabled: Boolean = true,
)
