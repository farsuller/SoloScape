package com.compose.soloscape.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.compose.soloscape.presentation.splash.SoloScapeSplash
import com.soloscape.util.routes.ScreensRoutes

fun NavGraphBuilder.splashRoute(
    onNavigateToDashboard: () -> Unit,
    isUpdateAvailable: Boolean,
) {
    composable(route = ScreensRoutes.SplashRoute.route) {
        SoloScapeSplash(
            onNavigateToDashboard = onNavigateToDashboard,
            isUpdateAvailable = isUpdateAvailable,
        )
    }
}
