package com.soloscape.idea.presentations.note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.soloscape.idea.presentations.note.components.TransparentTextField
import com.soloscape.database.domain.model.Note
import com.soloscape.idea.presentations.idea.components.MindPadFab
import com.soloscape.util.Constants.TestTags.CONTENT_TEXT_FIELD
import com.soloscape.util.Constants.TestTags.TITLE_TEXT_FIELD
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {},
) {
    val noteTitleState = viewModel.noteTitle.value
    val noteContentState = viewModel.noteContent.value

    val noteBackgroundAnimatable = remember {
        Animatable(
            initialValue = Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }

                is AddEditNoteViewModel.UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            MindPadFab(
                modifier = Modifier.testTag("Save"),
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                color = MaterialTheme.colorScheme.tertiary,
                imageVector = Icons.Filled.Save
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Arrow Icon",
                    )
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(Note.noteColors.size) {
                        val noteColors = Note.noteColors[it].toArgb()
                        SuggestionChip(
                            onClick = {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(noteColors),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(noteColors))
                            },
                            chipColor = Note.noteColors[it].toArgb(),
                            isSelected = viewModel.noteColor.value == noteColors
                        )
                    }
                }
            }


            Column(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TransparentTextField(
                    text = noteTitleState.text,
                    hint = noteTitleState.hint,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                    },
                    isHintVisible = noteTitleState.isHintVisible,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineMedium,
                    testTag = TITLE_TEXT_FIELD
                )

                TransparentTextField(
                    modifier = Modifier
                        .fillMaxHeight(),
                    text = noteContentState.text,
                    hint = noteContentState.hint,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                    },
                    isHintVisible = noteContentState.isHintVisible,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    testTag = CONTENT_TEXT_FIELD
                )
            }

        }
    }

}

@Composable
fun SuggestionChip(
    onClick: () -> Unit,
    isSelected: Boolean = true,
    chipColor: Int = MaterialTheme.colorScheme.tertiary.toArgb()
) {
    SuggestionChip(
        modifier = Modifier.padding(8.dp),
        onClick = onClick,
        label = {
            Spacer(modifier = Modifier.size(height = 30.dp, width = 20.dp))
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = Color(chipColor)
        ),
        border = SuggestionChipDefaults.suggestionChipBorder(
            enabled = isSelected,
            borderWidth = 1.dp,
            borderColor = Color.Black
        )
    )
}
