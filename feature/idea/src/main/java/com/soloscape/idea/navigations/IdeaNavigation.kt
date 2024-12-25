package com.soloscape.idea.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.soloscape.idea.presentations.idea.IdeaScreen
import com.soloscape.util.routes.ScreensRoutes

fun NavGraphBuilder.ideaRoute(
    navController: NavHostController
){
    composable(ScreensRoutes.IdeaRoute.route) {
        IdeaScreen(navController = navController)
    }
}