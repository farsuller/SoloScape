package com.compose.soloscape.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.compose.soloscape.presentation.splash.SoloScapeSplash
import com.soloscape.util.routes.ScreensRoutes
import kotlinx.coroutines.delay

fun NavGraphBuilder.splashRoute(
    onNavigateToDashboard: () -> Unit,
    isUpdateAvailable: Boolean,
) {
    composable(route = ScreensRoutes.SplashRoute.route) {

        LaunchedEffect(!isUpdateAvailable) {
            delay(2000)
            if (!isUpdateAvailable) onNavigateToDashboard()
        }

        SoloScapeSplash()
    }
}
