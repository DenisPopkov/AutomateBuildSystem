package com.bb.builds.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bb.builds.components.dialogs.BBTextField
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.getColorSystem

@Composable
fun <T> FilterableDropdownMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    items: List<T>,
    selectedIndex: Int,
    onItemSelected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (T, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        LargeDropdownMenuItem(
            text = item.toString(),
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("develop") }
    var selectedItemText by remember { mutableStateOf("") }
    val filteredItems =
        items.filter { selectedItemToString(it).contains(searchQuery, ignoreCase = true) }
    val colors = getColorSystem()

    Box(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(
                    text = label,
                    color = colors.black100,
                )
            },
            value = selectedItemText,
            enabled = enabled,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = MavenFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 22.2.sp,
                letterSpacing = (-0.5).sp,
                color = colors.black100,
                textAlign = TextAlign.Start
            ),
            trailingIcon = {
                val icon = if (expanded) {
                    Icons.Filled.ArrowDropUp
                } else {
                    Icons.Filled.ArrowDropDown
                }
                Icon(
                    modifier = Modifier
                        .clickable { searchQuery = "" },
                    imageVector = icon,
                    contentDescription = null,
                )
            },
            onValueChange = {},
            readOnly = true,
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .clickable(enabled = enabled) {
                    expanded = true
                },
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        Dialog(
            onDismissRequest = {
                expanded = false
            },
        ) {
            Surface(
                modifier = Modifier
                    .padding(top = 86.dp)
                    .background(color = colors.white100),
                shape = RoundedCornerShape(size = 12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.LightGray,
                )
            ) {
                Column(
                    modifier = Modifier
                        .background(color = colors.white100),
                ) {
                    BBTextField(
                        modifier = Modifier
                            .width(width = 266.dp)
                            .padding(all = 8.dp),
                        selectedValue = searchQuery,
                        onValueChange = { searchQuery = it },
                    )

                    val listState = rememberLazyListState()
                    if (selectedIndex > -1) {
                        LaunchedEffect("ScrollToSelected") {
                            listState.scrollToItem(index = selectedIndex)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .width(width = 266.dp)
                            .heightIn(max = 150.dp),
                        state = listState,
                    ) {
                        itemsIndexed(items = filteredItems) { index, item ->
                            val selectedItem = index == selectedIndex
                            drawItem(
                                item,
                                selectedItem,
                                true
                            ) {
                                onItemSelected(index, item)
                                selectedItemText = item.toString()
                                expanded = false
                            }

                            if (index < filteredItems.lastIndex) {
                                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LargeDropdownMenuItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val colors = getColorSystem()
    val contentColor = when {
        !enabled -> MaterialTheme.colors.onSurface.copy(alpha = 0f)
        selected -> colors.main
        else -> MaterialTheme.colors.onSurface.copy(alpha = 1f)
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier = Modifier
                .clickable(enabled) { onClick() }
                .fillMaxWidth()
                .padding(all = 16.dp),
        ) {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2,
                color = colors.black100,
            )
        }
    }

}
