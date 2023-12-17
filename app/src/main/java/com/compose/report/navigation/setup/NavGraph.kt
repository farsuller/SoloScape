package com.compose.report.navigation.setup

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.compose.report.navigation.routes.ScreensRoutes
import com.compose.report.navigation.authenticationRoute
import com.compose.report.navigation.homeRoute
import com.compose.report.navigation.reportRoute

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SetupNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    onDataLoaded: () -> Unit
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
            navigationToWrite = {
                navHostController.navigate(ScreensRoutes.Report.route)
            },
            navigateToWriteWithArgs = {
                navHostController.navigate(ScreensRoutes.Report.passReportId(reportId = it))
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