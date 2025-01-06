package com.soloscape.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun GenericFloatingActionButton(
    onClick: () -> Unit,
    testTag: String = "",
) {
    FloatingActionButton(
        modifier = Modifier.testTag(testTag),
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Write Icon",
        )
    }
}
