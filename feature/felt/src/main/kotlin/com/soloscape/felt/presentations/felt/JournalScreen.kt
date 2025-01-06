package com.soloscape.felt.presentations.felt

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.soloscape.felt.presentations.felt.components.JournalState
import com.soloscape.felt.presentations.felt.components.JournalTopBar
import com.soloscape.ui.components.GenericFloatingActionButton
import com.soloscape.util.Constants.TestTags.GENERIC_FAB_NAVIGATE
import java.time.ZonedDateTime

@Composable
fun JournalScreen(
    journalState: JournalState,
    navigateToWrite: () -> Unit = {},
    navigateToWriteWithArgs: (Int?) -> Unit = {},
    dateIsSelected: Boolean = false,
    onDateSelected: (ZonedDateTime) -> Unit = {},
    onDateReset: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    onDeleteAllConfirmed: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            JournalTopBar(
                scrollBehavior = scrollBehavior,
                onDateReset = onDateReset,
                onDateSelected = onDateSelected,
                dateIsSelected = dateIsSelected,
                onBackPressed = onBackPressed,
                onDeleteAllConfirmed = onDeleteAllConfirmed,
            )
        },
        floatingActionButton = {
            GenericFloatingActionButton(
                onClick = navigateToWrite,
                testTag = GENERIC_FAB_NAVIGATE,
            )
        },
        content = {
            JournalContent(
                paddingValues = it,
                writes = journalState.writes,
                onClickCard = navigateToWriteWithArgs,
            )
        },
    )
}
