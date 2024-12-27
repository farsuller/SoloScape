package com.compose.soloscape.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.soloscape.dashboard.navigation.dashboardRoute
import com.soloscape.felt.navigations.feltRoute
import com.soloscape.felt.navigations.writeRoute
import com.soloscape.idea.navigations.ideaRoute
import com.soloscape.idea.navigations.noteIdeaRoute
import com.soloscape.util.routes.ScreensRoutes

@Composable
fun SetupNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    onDataLoaded: (Boolean) -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
    ) {
        dashboardRoute(
            onDataLoaded = onDataLoaded,
            navigationToFelt = { navHostController.navigate(ScreensRoutes.FeltRoute.route) },
            navigationToIdea = { navHostController.navigate(ScreensRoutes.IdeaRoute.route) },
        )

        feltRoute(
            navigationToWrite = {
                navHostController.navigate(ScreensRoutes.WriteFeltRoute.route)
            },
            navigateToWriteWithArgs = {
                navHostController.navigate(ScreensRoutes.WriteFeltRoute.passWriteId(writeId = it ?: -1))
            },
            onBackPressed = {
                navHostController.popBackStack()
            },
        )
        writeRoute(
            onBackPressed = {
                navHostController.popBackStack()
            },
        )

        ideaRoute(
            navigateToNoteWithArgs = { note ->
                navHostController.navigate(ScreensRoutes.NoteIdeaRoute.passNoteId(noteId = note.noteId, noteColor = note.noteColor))
            },
            navigateToNote = {
                navHostController.navigate(ScreensRoutes.NoteIdeaRoute.route)
            },
            onBackPressed = {
                navHostController.popBackStack()
            },
        )

        noteIdeaRoute(onBackPressed = {
            navHostController.popBackStack()
        })
    }
}
