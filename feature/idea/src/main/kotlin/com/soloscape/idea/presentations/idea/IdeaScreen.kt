package com.soloscape.idea.presentations.idea

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.soloscape.database.domain.model.Note
import com.soloscape.idea.presentations.idea.components.IdeaState
import com.soloscape.idea.presentations.note.components.NoteIdColor
import com.soloscape.ui.components.GenericFloatingActionButton
import com.soloscape.ui.components.GenericSnackBar
import com.soloscape.ui.components.GenericTopAppBar
import com.soloscape.util.clickableWithoutRipple

@Composable
fun IdeaScreen(
    navigateToNoteWithArgs: (NoteIdColor) -> Unit,
    navigateToNote: () -> Unit,
    onBackPressed: () -> Unit,
    onDeleteClick: (Note) -> Unit,
    ideaState: IdeaState,
    snackBarHostState: SnackbarHostState,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            GenericSnackBar(snackBarHostState)
        },
        topBar = {
            GenericTopAppBar(
                scrollBehavior = scrollBehavior,
                iconModifier = Modifier
                    .padding(10.dp)
                    .clickableWithoutRipple { onBackPressed() },
            )
        },
        floatingActionButton = {
            GenericFloatingActionButton(onClick = navigateToNote)
        },
    ) { innerPadding ->

        IdeaContent(
            innerPadding = innerPadding,
            ideaState = ideaState,
            navigateToNoteWithArgs = navigateToNoteWithArgs,
            onDeleteClick = {
                onDeleteClick(it)
            },
        )
    }
}
