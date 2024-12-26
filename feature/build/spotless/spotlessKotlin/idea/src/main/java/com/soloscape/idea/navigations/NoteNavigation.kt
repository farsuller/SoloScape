package com.soloscape.idea.navigations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.soloscape.idea.presentations.note.AddEditNoteScreen
import com.soloscape.util.Constants.Routes.NOTE_COLOR_ARG_KEY
import com.soloscape.util.Constants.Routes.NOTE_ID_ARG_KEY
import com.soloscape.util.ScaleTransitionDirection
import com.soloscape.util.routes.ScreensRoutes
import com.soloscape.util.scaleIntoContainer
import com.soloscape.util.scaleOutOfContainer

fun NavGraphBuilder.noteIdeaRoute(
    navController: NavController,
) {
    composable(
        route = ScreensRoutes.NoteIdeaRoute.route,
        arguments = listOf(
            navArgument(
                name = NOTE_ID_ARG_KEY,
            ) {
                type = NavType.IntType
                defaultValue = -1
            },
            navArgument(
                name = NOTE_COLOR_ARG_KEY,
            ) {
                type = NavType.IntType
                defaultValue = -1
            },
        ),
        enterTransition = {
            scaleIntoContainer()
        },
        exitTransition = {
            scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
        },
        popEnterTransition = {
            scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
        },
        popExitTransition = {
            scaleOutOfContainer()
        },
    ) {
        val color = it.arguments?.getInt(NOTE_COLOR_ARG_KEY) ?: -1
        AddEditNoteScreen(
            navController = navController,
            noteColor = color,
            onBackPressed = {
                navController.popBackStack()
            },
        )
    }
}
