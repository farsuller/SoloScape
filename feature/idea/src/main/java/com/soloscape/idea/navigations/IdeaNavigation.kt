package com.soloscape.idea.navigations

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.soloscape.database.domain.model.Note
import com.soloscape.idea.presentations.idea.IdeaScreen
import com.soloscape.idea.presentations.idea.IdeaViewModel
import com.soloscape.idea.presentations.idea.components.IdeaEvent
import com.soloscape.idea.presentations.note.components.NoteIdColor
import com.soloscape.ui.components.DisplayAlertDialog
import com.soloscape.util.routes.ScreensRoutes
import kotlinx.coroutines.launch

fun NavGraphBuilder.ideaRoute(
    navigateToNoteWithArgs: (NoteIdColor) -> Unit,
    navigateToNote: () -> Unit,
    onBackPressed: () -> Unit,
) {
    composable(ScreensRoutes.IdeaRoute.route) {
        val viewModel: IdeaViewModel = hiltViewModel()
        val ideaState by viewModel.ideaState.collectAsStateWithLifecycle()
        var deleteNoteDialogOpened by remember { mutableStateOf(false) }
        var noteDeleteSelected by remember { mutableStateOf<Note?>(null) }
        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        IdeaScreen(
            navigateToNoteWithArgs = navigateToNoteWithArgs,
            navigateToNote = navigateToNote,
            onBackPressed = onBackPressed,
            ideaState = ideaState,
            onDeleteClick = { note ->
                noteDeleteSelected = note
                deleteNoteDialogOpened = true
            },
            snackBarHostState = snackBarHostState,
        )

        DisplayAlertDialog(
            title = "Delete ${noteDeleteSelected?.title}",
            message = "Do you really want to delete this?",
            dialogOpened = deleteNoteDialogOpened,
            onCloseDialog = { deleteNoteDialogOpened = false },
            onYesClicked = {
                noteDeleteSelected?.let {
                    viewModel.onEvent(IdeaEvent.DeleteNote(it))
                }

                scope.launch {
                    val result = snackBarHostState.showSnackbar(
                        message = "Note deleted",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(IdeaEvent.RestoreNote)
                    }
                }
            },
        )
    }
}
