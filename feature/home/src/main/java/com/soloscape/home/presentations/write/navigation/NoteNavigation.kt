package com.soloscape.home.presentations.write.navigation

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.soloscape.home.presentations.write.NoteScreen
import com.soloscape.home.presentations.write.WriteViewModel
import com.soloscape.home.presentations.write.components.WriteEvent
import com.soloscape.ui.Mood
import com.soloscape.util.Constants.NOTE_SCREEN_ARG_KEY
import com.soloscape.util.ScreensRoutes

fun NavGraphBuilder.reportRoute(onBackPressed: () -> Unit) {
    composable(
        route = ScreensRoutes.Note.route,
        arguments = listOf(
            navArgument(name = NOTE_SCREEN_ARG_KEY) {
                type = NavType.IntType
                defaultValue = -1
            },
        ),
    ) {
        val viewModel: WriteViewModel = hiltViewModel()
        val uiState = viewModel.uiState
        val pagerState = rememberPagerState(pageCount = { Mood.entries.size })
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }

        val noteTitleState = viewModel.noteTitle
        val noteContentState = viewModel.noteContent

        NoteScreen(
            onBackPressed = onBackPressed,
            pagerState = pagerState,
            uiState = uiState,
            moodName = { Mood.entries[pageNumber].name },
            onSaveClicked = {
                viewModel.onEvent(WriteEvent.UpsertWriteItem(onSuccess = onBackPressed))
            },
            onDateTimeUpdated = {
                // viewModel.updateDateTime(zonedDateTime = it)
            },
            onDeleteConfirmed = { write ->
                viewModel.onEvent(
                    WriteEvent.DeleteWriteItem(writeItem = write, onSuccess = onBackPressed)
                )
            },
            noteTitleState = noteTitleState,
            noteContentState = noteContentState,
            onValueChangeTitle = { viewModel.onEvent(WriteEvent.EnteredTitle(it)) },
            onFocusChangeTitle = { viewModel.onEvent(WriteEvent.ChangeTitleFocus(it)) },
            onValueChangeContent = { viewModel.onEvent(WriteEvent.EnteredContent(it)) },
            onFocusChangeContent = { viewModel.onEvent(WriteEvent.ChangeContentFocus(it)) },
        )
    }
}
