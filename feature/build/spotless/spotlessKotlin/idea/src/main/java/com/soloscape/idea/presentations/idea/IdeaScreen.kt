package com.soloscape.idea.presentations.idea

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.soloscape.idea.presentations.idea.components.MindPadFab
import com.soloscape.idea.presentations.idea.components.NoteItem
import com.soloscape.idea.presentations.idea.components.OrderSectionCard
import com.soloscape.util.Constants.TestTags.ORDER_SECTION_CARD
import com.soloscape.util.clickableWithoutRipple
import com.soloscape.util.routes.ScreensRoutes
import kotlinx.coroutines.launch

@Composable
fun IdeaScreen(
    navController: NavController,
) {
    val viewModel: IdeaViewModel = hiltViewModel()
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            MindPadFab(
                modifier = Modifier.testTag("Add"),
                onClick = {
                    navController.navigate(ScreensRoutes.NoteIdeaRoute.route)
                },
                color = MaterialTheme.colorScheme.tertiary,
                imageVector = Icons.Outlined.Add,
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                )
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Text(
                    text = "MindPad",
                    style = MaterialTheme.typography.headlineMedium,
                )

                if (state.notes.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.onEvent(IdeaEvent.ToggleOrderSection) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Sort,
                            contentDescription = "Sort",
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
            ) {
                OrderSectionCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .testTag(ORDER_SECTION_CARD),
                    noteOrder = state.noteOrder,
                    onOrderChange = { noteOrder ->
                        viewModel.onEvent(IdeaEvent.Order(noteOrder))
                    },
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (state.notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Take note what's on your mind.")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.notes) { note ->
                        NoteItem(
                            note = note,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableWithoutRipple {
                                    navController.navigate(
                                        ScreensRoutes.NoteIdeaRoute.passNoteId(
                                            noteId = note.id,
                                            noteColor = note.color,
                                        ),
                                    )
                                },
                            onDeleteClick = {
                                viewModel.onEvent(IdeaEvent.DeleteNote(note))

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
            }
        }
    }
}
