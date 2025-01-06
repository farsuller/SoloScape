package com.soloscape.felt.navigations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.soloscape.felt.presentations.felt.JournalScreen
import com.soloscape.felt.presentations.felt.JournalViewModel
import com.soloscape.felt.presentations.felt.components.JournalEvent
import com.soloscape.util.routes.ScreensRoutes

fun NavGraphBuilder.journalRoute(
    navigationToWrite: () -> Unit,
    navigateToWriteWithArgs: (Int?) -> Unit,
    onBackPressed: () -> Unit,
) {
    composable(route = ScreensRoutes.FeltRoute.route) {
        val viewModel: JournalViewModel = hiltViewModel()
        val homeState by viewModel.journalState.collectAsStateWithLifecycle()

        JournalScreen(
            journalState = homeState,
            navigateToWrite = navigationToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs,
            dateIsSelected = viewModel.dateIsSelected,
            onDateSelected = { viewModel.onEvent(JournalEvent.FilterBySelectedDate(dateTime = it)) },
            onDateReset = { viewModel.onEvent(JournalEvent.DisplayAllDate) },
            onBackPressed = onBackPressed,
            onDeleteAllConfirmed = { viewModel.onEvent(JournalEvent.DeleteAll) },
        )
    }
}
