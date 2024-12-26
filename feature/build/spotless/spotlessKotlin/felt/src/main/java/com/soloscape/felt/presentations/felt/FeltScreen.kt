package com.soloscape.felt.presentations.felt

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.LayoutDirection
import com.soloscape.felt.presentations.felt.components.FeltState
import com.soloscape.felt.presentations.felt.components.FeltTopBar
import java.time.ZonedDateTime

@Composable
internal fun FeltScreen(
    homeState: FeltState,
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (Int?) -> Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit,
    onBackPressed: () -> Unit,
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FeltTopBar(
                scrollBehavior = scrollBehavior,
                onDateReset = onDateReset,
                onDateSelected = onDateSelected,
                dateIsSelected = dateIsSelected,
                onBackPressed = onBackPressed,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                onClick = navigateToWrite,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Write Icon",
                )
            }
        },
        content = {
            padding = it
            FeltContent(
                paddingValues = it,
                writes = homeState.writes,
                onClick = navigateToWriteWithArgs,
            )
        },
    )
}
