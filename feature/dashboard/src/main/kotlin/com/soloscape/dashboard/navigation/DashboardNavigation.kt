package com.soloscape.dashboard.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.soloscape.dashboard.presentation.dashboard.DashboardScreen
import com.soloscape.dashboard.presentation.dashboard.DashboardViewModel
import com.soloscape.dashboard.presentation.dashboard.YourNameEvent
import com.soloscape.util.routes.ScreensRoutes

fun NavGraphBuilder.dashboardRoute(
    navigationToFelt: () -> Unit,
    navigationToIdea: () -> Unit,
) {
    composable(route = ScreensRoutes.DashboardRoute.route) {
        val viewModel: DashboardViewModel = hiltViewModel()
        val yourNameState by viewModel.yourNameState.collectAsStateWithLifecycle()
        val journalNoteState by viewModel.journalNoteState.collectAsStateWithLifecycle()

        DashboardScreen(
            navigationToFelt = navigationToFelt,
            navigationToIdea = navigationToIdea,
            yourNameState = yourNameState,
            journalNoteState = journalNoteState,
            onValueChange = { viewModel.onEvent(YourNameEvent.EnteredYourName(it)) },
            onFocusChange = { viewModel.onEvent(YourNameEvent.ChangeTextFocus(it)) },
            onJournalClick = navigationToFelt,
            onNoteClick = navigationToIdea,
        )
    }
}
