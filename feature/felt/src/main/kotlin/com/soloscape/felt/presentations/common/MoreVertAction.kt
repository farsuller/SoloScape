package com.soloscape.felt.presentations.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.soloscape.database.domain.model.Journal
import com.soloscape.ui.components.DisplayAlertDialog
import com.soloscape.ui.theme.robotoBoldFontFamily
import com.soloscape.util.clickableWithoutRipple

@Composable
fun MoreVertAction(
    selectedWrite: Journal? = null,
    onDeleteConfirmed: () -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var openDialog by remember {
        mutableStateOf(false)
    }
    val deleteItem = selectedWrite?.title ?: "All"

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(text = {
            Text(
                text = "Delete",
                style = TextStyle(
                    fontFamily = robotoBoldFontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                ),
            )
        }, onClick = {
            openDialog = true
            expanded = false
        })
    }

    DisplayAlertDialog(
        title = "Delete $deleteItem",
        message = "Do you really want to delete this?",
        dialogOpened = openDialog,
        onCloseDialog = { openDialog = false },
        onYesClicked = onDeleteConfirmed,
    )

    Icon(
        modifier = Modifier
            .padding(end = 5.dp)
            .clickableWithoutRipple(
                onClick = { expanded = !expanded },
            ),
        imageVector = Icons.Default.MoreVert,
        contentDescription = "Overflow Menu Icon",
        tint = MaterialTheme.colorScheme.onSurface,
    )
}
