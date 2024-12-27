package com.soloscape.idea.presentations.idea

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.soloscape.database.domain.model.Note
import com.soloscape.idea.presentations.idea.components.IdeaState
import com.soloscape.idea.presentations.idea.components.NoteItemCard
import com.soloscape.idea.presentations.note.components.NoteIdColor
import com.soloscape.ui.components.EmptyListContainer
import com.soloscape.util.clickableWithoutRipple

@Composable
fun IdeaContent(
    innerPadding: PaddingValues,
    ideaState: IdeaState,
    navigateToNoteWithArgs: (NoteIdColor) -> Unit,
    onDeleteClick: (Note) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
            )
            .padding(16.dp),
    ) {
        if (ideaState.notes.isEmpty()) {
            EmptyListContainer(title = "Got an idea?", subtitle = "Note it down!")
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(ideaState.notes) { note ->
                        NoteItemCard(
                            note = note,
                            modifier = Modifier
                                .width(200.dp)
                                .wrapContentHeight()
                                .clickableWithoutRipple {
                                    navigateToNoteWithArgs(
                                        NoteIdColor(
                                            noteId = note.id,
                                            noteColor = note.color,
                                        ),
                                    )
                                },
                            onDeleteClick = {
                                onDeleteClick(note)
                            },
                        )
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
