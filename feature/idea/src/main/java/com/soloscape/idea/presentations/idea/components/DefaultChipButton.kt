package com.soloscape.idea.presentations.idea.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soloscape.ui.theme.SoloScapeTheme

@Composable
fun DefaultChipButton(
    modifier: Modifier = Modifier,
    text: String = "",
    isSelected: Boolean = false,
    onSelectionChanged: () -> Unit = {},
){
    FilterChip(
        onClick = onSelectionChanged,
        label = {
            Text(text)
        },
        selected = isSelected,
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Check icon",
                    tint = Color.White,
                    modifier = modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        colors = FilterChipDefaults.filterChipColors(
            labelColor = MaterialTheme.colorScheme.tertiary,
            selectedLabelColor = MaterialTheme.colorScheme.primary,
            selectedContainerColor = MaterialTheme.colorScheme.tertiary),

        border = FilterChipDefaults.filterChipBorder(
            enabled = isSelected,
            selected = isSelected,
            disabledBorderColor = MaterialTheme.colorScheme.tertiary,
        )
    )
}

@Preview(showBackground = true)
@Composable
fun FilterChipPreview(){
    SoloScapeTheme {
        Surface {
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DefaultChipButton(text = "Test", isSelected = true)
                DefaultChipButton(text = "Test", isSelected = false)
            }

        }
    }
}
