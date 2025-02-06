package com.bb.builds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automatebuildsystem.composeapp.generated.resources.Res
import automatebuildsystem.composeapp.generated.resources.ic_clear_medium
import com.bb.builds.theme.MavenFontFamily
import com.bb.builds.theme.getColorSystem
import org.jetbrains.compose.resources.painterResource

@Composable
fun BBTextField(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    focusRequester: FocusRequester = FocusRequester(),
    onValueChange: (String) -> Unit,
    onFocusChanged: (state: FocusState) -> Unit = {},
    placeholder: String = ""
) {
    val colors = getColorSystem()
    val customTextSelectionColors = TextSelectionColors(
        handleColor = colors.main,
        backgroundColor = colors.main.copy(alpha = 0.2f)
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
                        color = colors.black10,
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
                        color = colors.black70,
                        textAlign = TextAlign.Start
                    ),
                    cursorBrush = SolidColor(colors.main),
                )

                if (textFieldValue.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(
                            fontFamily = MavenFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            lineHeight = 22.2.sp,
                            letterSpacing = (-0.5).sp,
                            color = colors.black40,
                            textAlign = TextAlign.Start
                        ),
                    )
                }

                if (textFieldValue.isNotEmpty()) {
                    Icon(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterEnd)
                            .clip(shape = CircleShape)
                            .clickable(
                                onClick = {
                                    onValueChange.invoke("")
                                },
                            )
                            .padding(all = 8.dp),
                        painter = painterResource(Res.drawable.ic_clear_medium),
                        contentDescription = null,
                        tint = colors.black70,
                    )
                }
            }
        }
    }
}
