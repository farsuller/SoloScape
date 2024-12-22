package com.soloscape.home.presentations.write.navigation

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.soloscape.home.presentations.write.NoteScreen
import com.soloscape.home.presentations.write.NoteViewModel
import com.soloscape.home.presentations.write.WriteEvent
import com.soloscape.ui.Mood
import com.soloscape.util.Constants.NOTE_SCREEN_ARG_KEY
import com.soloscape.util.ScreensRoutes

fun NavGraphBuilder.reportRoute(onBackPressed: () -> Unit) {
    composable(
        route = ScreensRoutes.Note.route,
        arguments = listOf(
            navArgument(name = NOTE_SCREEN_ARG_KEY) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
        ),
    ) {
        val viewModel: NoteViewModel = hiltViewModel()
        val uiState = viewModel.uiState
        val pagerState = rememberPagerState(pageCount = { Mood.values().size })
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }

        NoteScreen(
            onBackPressed = onBackPressed,
            pagerState = pagerState,
            uiState = uiState,
            moodName = { Mood.entries[pageNumber].name },
            onSaveClicked = { write ->

                viewModel.onEvent(WriteEvent.UpsertWriteItem(writeItem = write, onSuccess = onBackPressed))
//                viewModel.insertUpdateNotes(
//                    report = it.apply { },
//                    onSuccess = { onBackPressed() },
//                    onError = { message ->
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//                    },
//                )
            },
            onDateTimeUpdated = {
               // viewModel.updateDateTime(zonedDateTime = it)
            },
            onDeleteConfirmed = { write ->
                viewModel.onEvent(
                    WriteEvent.DeleteWriteItem(writeItem = write, onSuccess = onBackPressed)
                )
//                viewModel.deleteNotes(
//                    onSuccess = {
//                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
//                        onBackPressed()
//                    },
//                    onError = { message ->
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//                    },
//                )
            },
        )
    }
}
