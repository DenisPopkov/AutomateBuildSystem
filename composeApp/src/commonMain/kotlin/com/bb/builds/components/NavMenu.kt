package com.bb.builds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun NavMenu(
    modifier: Modifier = Modifier,
    selectedDefaultItem: String = "Create",
    onCreateScreen: () -> Unit,
    onBuildsScreen: () -> Unit,
) {
    var selectedItem by remember { mutableStateOf(selectedDefaultItem) }

    Row(
        modifier = modifier
            .size(width = 230.dp, height = 50.dp)
            .clip(shape = CircleShape)
            .background(color = Color.LightGray.copy(alpha = 0.6f))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NavMenuItem(
            menuItemText = "Create",
            isSelected = selectedItem == "Create",
            onClick = {
                selectedItem = "Create"
                onCreateScreen.invoke()
            }
        )
        NavMenuItem(
            menuItemText = "Builds",
            isSelected = selectedItem == "Builds",
            onClick = {
                selectedItem = "Builds"
                onBuildsScreen.invoke()
            }
        )
    }
}

@Composable
fun NavMenuItem(
    menuItemText: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(shape = CircleShape)
            .background(color = if (isSelected) Color.Gray.copy(alpha = 0.6f) else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = menuItemText,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            letterSpacing = 0.4.sp
        )
    }
}
