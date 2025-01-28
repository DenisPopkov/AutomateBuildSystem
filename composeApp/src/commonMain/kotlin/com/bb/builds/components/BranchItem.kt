package com.bb.builds.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bb.builds.components.theme.MavenFontFamily
import com.bb.builds.components.theme.Theme
import com.bb.builds.components.theme.getColorSystem

@Composable
fun BranchItem(
    branch: String,
    branchColor: Color,
    showDivider: Boolean,
    onSelectBranchClick: (String) -> Unit,
) {
    val colors = getColorSystem()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectBranchClick.invoke(branch) },
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = Theme.spacingSystem.s),
            text = branch,
            style = TextStyle(
                fontFamily = MavenFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 22.sp,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = branchColor,
        )

        if (showDivider) {
            Divider(
                thickness = 1.dp,
                color = colors.black20
            )
        }
    }
}