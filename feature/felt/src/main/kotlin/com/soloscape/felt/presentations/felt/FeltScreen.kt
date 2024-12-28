package com.soloscape.felt.presentations.felt

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.soloscape.felt.presentations.felt.components.FeltState
import com.soloscape.felt.presentations.felt.components.FeltTopBar
import com.soloscape.ui.components.GenericFloatingActionButton
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
    onDeleteAllConfirmed: () -> Unit,
) {
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
                onDeleteAllConfirmed = onDeleteAllConfirmed,
            )
        },
        floatingActionButton = {
            GenericFloatingActionButton(onClick = navigateToWrite)
        },
        content = {
            FeltContent(
                paddingValues = it,
                writes = homeState.writes,
                onClickCard = navigateToWriteWithArgs,
            )
        },
    )
}
