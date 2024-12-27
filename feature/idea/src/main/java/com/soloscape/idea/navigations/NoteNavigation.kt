package com.soloscape.idea.navigations

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.soloscape.idea.presentations.note.NoteScreen
import com.soloscape.idea.presentations.note.NoteViewModel
import com.soloscape.idea.presentations.note.components.NoteEvent
import com.soloscape.util.Constants.Routes.NOTE_COLOR_ARG_KEY
import com.soloscape.util.Constants.Routes.NOTE_ID_ARG_KEY
import com.soloscape.util.ScaleTransitionDirection
import com.soloscape.util.routes.ScreensRoutes
import com.soloscape.util.scaleIntoContainer
import com.soloscape.util.scaleOutOfContainer
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarPosition
import com.stevdzasan.messagebar.rememberMessageBarState

fun NavGraphBuilder.noteIdeaRoute(
    onBackPressed: () -> Unit,
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
        val viewModel: NoteViewModel = hiltViewModel()
        val noteState by viewModel.noteState.collectAsStateWithLifecycle()
        val messageBarState = rememberMessageBarState()

        val titleEmpty = noteState.title.isEmpty()
        val contentEmpty = noteState.content.isEmpty()

        ContentWithMessageBar(
            messageBarState = messageBarState,
            showCopyButton = false,
            position = MessageBarPosition.BOTTOM,
        ) {
            NoteScreen(
                noteColor = color,
                onBackPressed = onBackPressed,
                onSaveClicked = {
                    when {
                        !titleEmpty && !contentEmpty -> {
                            viewModel.onEvent(NoteEvent.SaveNote(onSuccess = onBackPressed))
                        }

                        titleEmpty && !contentEmpty -> messageBarState.addError(Exception("Title cannot be empty"))
                        !titleEmpty && contentEmpty -> messageBarState.addError(Exception("Content cannot be empty."))
                        else -> messageBarState.addError(Exception("Fields cannot be empty."))
                    }
                },
                noteState = noteState,
                onValueChangeTitle = { viewModel.onEvent(NoteEvent.EnteredTitle(it)) },
                onFocusChangeTitle = { viewModel.onEvent(NoteEvent.ChangeTitleFocus(it)) },
                onValueChangeContent = { viewModel.onEvent(NoteEvent.EnteredContent(it)) },
                onFocusChangeContent = { viewModel.onEvent(NoteEvent.ChangeContentFocus(it)) },
                onChangeColor = { viewModel.onEvent(NoteEvent.ChangeColor(it)) },
            )
        }
    }
}
