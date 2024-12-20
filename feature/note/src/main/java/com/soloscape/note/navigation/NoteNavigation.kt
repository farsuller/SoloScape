package com.soloscape.note.navigation

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.soloscape.note.NoteScreen
import com.soloscape.note.NoteViewModel
import com.soloscape.util.Constants.NOTE_SCREEN_ARG_KEY
import com.soloscape.util.ScreensRoutes
import com.soloscape.util.model.Mood

@OptIn(ExperimentalFoundationApi::class)
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
        val context = LocalContext.current
        val galleryState = viewModel.galleryState
        NoteScreen(
            onBackPressed = onBackPressed,
            pagerState = pagerState,
            uiState = uiState,
            onNoteChange = { note ->
                viewModel.setTitle(title = note.title)
                viewModel.setDescription(description = note.description)
            },
            moodName = { Mood.values()[pageNumber].name },
            onSaveClicked = {
                viewModel.insertUpdateNotes(
                    report = it.apply { mood = Mood.values()[pageNumber].name },
                    onSuccess = { onBackPressed() },
                    onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    },
                )
            },
            onDateTimeUpdated = {
                viewModel.updateDateTime(zonedDateTime = it)
            },
            onDeleteConfirmed = {
                viewModel.deleteNotes(
                    onSuccess = {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    },
                    onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    },
                )
            },
            galleryState = galleryState,
            onImageSelect = {
                val type = context.contentResolver.getType(it)?.split("/")?.last() ?: "jpg"
                viewModel.addImage(image = it, imageType = type)
            },
            onImageDeleteClicked = { galleryState.removeImage(it) },
        )
    }
}
