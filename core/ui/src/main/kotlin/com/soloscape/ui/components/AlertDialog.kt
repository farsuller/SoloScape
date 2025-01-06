package com.soloscape.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import com.soloscape.ui.theme.robotoBoldFontFamily

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    dialogOpened: Boolean,
    onCloseDialog: () -> Unit,
    onYesClicked: () -> Unit,
    testTagYes: String = "",
) {
    if (dialogOpened) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = robotoBoldFontFamily,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize,
                        fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                    ),
                )
            },
            text = {
                Text(
                    text = message,
                    style = TextStyle(
                        fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    ),
                )
            },
            confirmButton = {
                Button(
                    modifier = Modifier.testTag(testTagYes),
                    onClick = {
                        onYesClicked()
                        onCloseDialog()
                    },
                ) {
                    Text(
                        text = "Yes",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                            fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                        ),
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onCloseDialog,
                ) {
                    Text(
                        text = "No",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                            fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                        ),
                    )
                }
            },
            onDismissRequest = onCloseDialog,
        )
    }
}
