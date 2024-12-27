package com.soloscape.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.soloscape.ui.theme.robotoBoldFontFamily

@Composable
fun GenericSnackBar(snackBarHostState: SnackbarHostState) {
    SnackbarHost(
        hostState = snackBarHostState,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        snackbar = { snackBarData ->
            Snackbar(
                action = {
                    snackBarData.visuals.actionLabel?.let { actionLabel ->
                        TextButton(onClick = { snackBarData.performAction() }) {
                            Text(
                                text = actionLabel,
                                style = TextStyle(
                                    fontFamily = robotoBoldFontFamily,
                                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                                    fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                                ),
                            )
                        }
                    }
                },
                content = {
                    Text(
                        text = snackBarData.visuals.message,
                        style = TextStyle(
                            fontFamily = robotoBoldFontFamily,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                        ),
                    )
                },
            )
        },
    )
}
