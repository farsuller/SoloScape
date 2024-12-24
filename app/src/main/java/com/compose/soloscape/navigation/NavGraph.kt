package com.compose.soloscape.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.soloscape.home.presentations.home.navigation.homeRoute
import com.soloscape.home.presentations.write.navigation.writeRoute
import com.soloscape.util.routes.ScreensRoutes

@Composable
fun SetupNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    onDataLoaded: () -> Unit,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
    ) {
//        authenticationRoute(
//            navigateToHome = {
//                navHostController.popBackStack()
//                navHostController.navigate(ScreensRoutes.Home.route)
//            },
//            onDataLoaded = onDataLoaded,
//        )
        homeRoute(
            darkTheme = darkTheme,
            onThemeUpdated = onThemeUpdated,
            navigationToWrite = {
                navHostController.navigate(ScreensRoutes.Note.route)
            },
            navigateToWriteWithArgs = {
                navHostController.navigate(ScreensRoutes.Note.passNoteId(noteId = it ?: -1))
            },
            navigateToAuth = {
                navHostController.popBackStack()
                navHostController.navigate(ScreensRoutes.Authentication.route)
            },
            onDataLoaded = onDataLoaded,
        )
        writeRoute(
            onBackPressed = {
                navHostController.popBackStack()
            },
        )
    }
}
