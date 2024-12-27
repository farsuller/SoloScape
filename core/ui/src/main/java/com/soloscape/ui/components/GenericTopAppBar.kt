package com.soloscape.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GenericTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    iconModifier: Modifier = Modifier,
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            Icon(
                modifier = iconModifier,
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "Close Icon",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        title = {},
    )
}
