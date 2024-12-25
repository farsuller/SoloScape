package com.soloscape.dashboard.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.soloscape.dashboard.presentation.dashboard.DashboardScreen
import com.soloscape.util.routes.ScreensRoutes

fun NavGraphBuilder.dashboardRoute(
    onDataLoaded: (Boolean) -> Unit,
    navigationToFelt: () -> Unit,
    navigationToIdea: () -> Unit,
) {
    composable(route = ScreensRoutes.DashboardRoute.route) {
        LaunchedEffect(key1 = Unit) {
            onDataLoaded(false)
        }

        DashboardScreen(
            navigationToFelt = navigationToFelt,
            navigationToIdea = navigationToIdea
        )
    }
}