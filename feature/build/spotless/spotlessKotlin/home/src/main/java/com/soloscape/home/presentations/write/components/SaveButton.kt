package com.soloscape.home.presentations.write.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soloscape.ui.R

@Composable
fun SaveButton(
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    FloatingActionButton(
        modifier = Modifier.size(55.dp),
        onClick = onClick,
        containerColor = color,
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = R.drawable.ic_write),
            contentDescription = "Save note",
            tint = Color.White,
        )
    }
}
