package com.soloscape.dashboard.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.soloscape.dashboard.presentation.dashboard.DashboardScreen
import com.soloscape.dashboard.presentation.dashboard.DashboardViewModel
import com.soloscape.dashboard.presentation.dashboard.components.YourNameEvent
import com.soloscape.util.routes.ScreensRoutes

fun NavGraphBuilder.dashboardRoute(
    onDataLoaded: (Boolean) -> Unit,
    navigationToFelt: () -> Unit,
    navigationToIdea: () -> Unit,
) {
    composable(route = ScreensRoutes.DashboardRoute.route) {
        val viewModel: DashboardViewModel = hiltViewModel()
        val yourNameState by viewModel.yourNameState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = Unit) {
            onDataLoaded(false)
        }

        DashboardScreen(
            navigationToFelt = navigationToFelt,
            navigationToIdea = navigationToIdea,
            yourNameState = yourNameState,
            onValueChange = { viewModel.onEvent(YourNameEvent.EnteredYourName(it)) },
            onFocusChange = { viewModel.onEvent(YourNameEvent.ChangeTextFocus(it)) },
        )
    }
}