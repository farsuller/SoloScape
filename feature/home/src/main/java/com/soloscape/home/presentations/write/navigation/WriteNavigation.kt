package com.soloscape.home.presentations.write.navigation

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.soloscape.home.presentations.write.WriteScreen
import com.soloscape.home.presentations.write.WriteViewModel
import com.soloscape.home.presentations.write.components.WriteEvent
import com.soloscape.ui.Reaction
import com.soloscape.util.Constants.NOTE_SCREEN_ARG_KEY
import com.soloscape.util.ScaleTransitionDirection
import com.soloscape.util.routes.ScreensRoutes
import com.soloscape.util.scaleIntoContainer
import com.soloscape.util.scaleOutOfContainer
import com.soloscape.util.toEpochMilliOrNull
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarPosition
import com.stevdzasan.messagebar.rememberMessageBarState
import java.lang.Exception

fun NavGraphBuilder.writeRoute(onBackPressed: () -> Unit) {
    composable(
        route = ScreensRoutes.Note.route,
        arguments = listOf(
            navArgument(name = NOTE_SCREEN_ARG_KEY) {
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
        }
    ) {
        val viewModel: WriteViewModel = hiltViewModel()
        val pagerState = rememberPagerState(pageCount = { Reaction.entries.size })
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }
        val messageBarState = rememberMessageBarState()
        val writeState by viewModel.writeState.collectAsStateWithLifecycle()

        val titleEmpty = writeState.title.isEmpty()
        val contentEmpty = writeState.content.isEmpty()

        val reaction = Reaction.entries[pageNumber]

        ContentWithMessageBar(
            messageBarState = messageBarState,
            showCopyButton = false,
            position = MessageBarPosition.BOTTOM,
        ) {
            WriteScreen(
                onBackPressed = onBackPressed,
                pagerState = pagerState,
                reaction = reaction,
                onSaveClicked = {
                    when {
                        !titleEmpty && !contentEmpty -> {
                            viewModel.onEvent(
                                WriteEvent.UpsertWriteItem(
                                    reaction = reaction,
                                    onSuccess = onBackPressed
                                )
                            )
                        }
                        titleEmpty && !contentEmpty -> messageBarState.addError(Exception("Title cannot be empty"))
                        !titleEmpty && contentEmpty -> messageBarState.addError(Exception("Content cannot be empty."))
                        else -> messageBarState.addError(Exception("Fields cannot be empty."))
                    }
                },
                onDateTimeUpdated = {
                    viewModel.onEvent(WriteEvent.SelectedDateTime(date = it.toEpochMilliOrNull()))
                },
                onDeleteConfirmed = { write ->
                    viewModel.onEvent(
                        WriteEvent.DeleteWriteItem(
                            writeItem = write,
                            onSuccess = onBackPressed,
                        ),
                    )
                },
                writeState = writeState,
                onValueChangeTitle = { viewModel.onEvent(WriteEvent.EnteredTitle(it)) },
                onFocusChangeTitle = { viewModel.onEvent(WriteEvent.ChangeTitleFocus(it)) },
                onValueChangeContent = { viewModel.onEvent(WriteEvent.EnteredContent(it)) },
                onFocusChangeContent = { viewModel.onEvent(WriteEvent.ChangeContentFocus(it)) },
            )
        }
    }
}
