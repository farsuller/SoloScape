package com.compose.soloscape.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.soloscape.dashboard.navigation.dashboardRoute
import com.soloscape.home.presentations.home.navigation.homeRoute
import com.soloscape.home.presentations.write.navigation.writeRoute
import com.soloscape.util.routes.ScreensRoutes

@Composable
fun SetupNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    onDataLoaded: (Boolean) -> Unit,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
    ) {

        dashboardRoute(
            onDataLoaded = onDataLoaded,
            navigationToFelt = { navHostController.navigate(ScreensRoutes.FeltRoute.route) },
            navigationToIdea = {

            },
        )

        homeRoute(
            darkTheme = darkTheme,
            onThemeUpdated = onThemeUpdated,
            navigationToWrite = {
                navHostController.navigate(ScreensRoutes.WriteFeltRoute.route)
            },
            navigateToWriteWithArgs = {
                navHostController.navigate(
                    ScreensRoutes.WriteFeltRoute.passWriteId(
                        noteId = it ?: -1
                    )
                )
            },
        )
        writeRoute(
            onBackPressed = {
                navHostController.popBackStack()
            },
        )
    }
}
