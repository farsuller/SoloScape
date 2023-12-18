package com.compose.soloscape.navigation.setup

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.compose.soloscape.navigation.routes.ScreensRoutes
import com.compose.soloscape.navigation.authenticationRoute
import com.compose.soloscape.navigation.homeRoute
import com.compose.soloscape.navigation.reportRoute

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SetupNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    onDataLoaded: () -> Unit,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        }
    )
    {
        authenticationRoute(
            navigateToHome = {
                navHostController.popBackStack()
                navHostController.navigate(ScreensRoutes.Home.route)
            },
            onDataLoaded = onDataLoaded
        )
        homeRoute(
            darkTheme = darkTheme,
            onThemeUpdated = onThemeUpdated,
            navigationToWrite = {
                navHostController.navigate(ScreensRoutes.Note.route)
            },
            navigateToWriteWithArgs = {
                navHostController.navigate(ScreensRoutes.Note.passNoteId(noteId = it))
            },
            navigateToAuth = {
                navHostController.popBackStack()
                navHostController.navigate(ScreensRoutes.Authentication.route)
            },
            onDataLoaded = onDataLoaded
        )
        reportRoute(
            onBackPressed = {
                navHostController.popBackStack()
            }
        )
    }
}