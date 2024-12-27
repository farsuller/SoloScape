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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.soloscape.database.domain.model.Note
import com.soloscape.idea.presentations.note.components.NoteState
import com.soloscape.ui.components.SaveButton
import com.soloscape.ui.components.TransparentTextField
import com.soloscape.util.Constants.TestTags.CONTENT_TEXT_FIELD
import com.soloscape.util.Constants.TestTags.TITLE_TEXT_FIELD
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    noteColor: Int,
    onBackPressed: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onChangeColor: (Int) -> Unit,
    onValueChangeTitle: (String) -> Unit,
    onFocusChangeTitle: (FocusState) -> Unit,
    onValueChangeContent: (String) -> Unit,
    onFocusChangeContent: (FocusState) -> Unit,
    noteState: NoteState,
) {
    val focusManager = LocalFocusManager.current

    val noteBackgroundAnimatable = remember {
        Animatable(
            initialValue = Color(if (noteColor != -1) noteColor else noteState.noteColor ?: 0),
        )
    }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            SaveButton(
                onClick = onSaveClicked,
                color = MaterialTheme.colorScheme.primary,
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
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
                                onChangeColor(noteColors)
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(noteColors),
                                        animationSpec = tween(
                                            durationMillis = 500,
                                        ),
                                    )
                                }
                            },
                            chipColor = Note.noteColors[it].toArgb(),
                            isSelected = noteState.noteColor == noteColors,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TransparentTextField(
                    text = noteState.title,
                    hint = noteState.titleHint,
                    onValueChange = onValueChangeTitle,
                    onFocusChange = onFocusChangeTitle,
                    isHintVisible = noteState.titleHintVisible,
                    textStyle = MaterialTheme.typography.headlineMedium,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                    ),
                    testTag = TITLE_TEXT_FIELD,
                )

                TransparentTextField(
                    modifier = Modifier
                        .fillMaxHeight(),
                    text = noteState.content,
                    hint = noteState.contentHint,
                    onValueChange = onValueChangeContent,
                    onFocusChange = onFocusChangeContent,
                    isHintVisible = noteState.contentHintVisible,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    testTag = CONTENT_TEXT_FIELD,
                )
            }
        }
    }
}

@Composable
fun SuggestionChip(
    onClick: () -> Unit,
    isSelected: Boolean = true,
    chipColor: Int = MaterialTheme.colorScheme.tertiary.toArgb(),
) {
    SuggestionChip(
        modifier = Modifier.padding(8.dp),
        onClick = onClick,
        label = {
            Spacer(modifier = Modifier.size(height = 30.dp, width = 20.dp))
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = Color(chipColor),
        ),
        border = SuggestionChipDefaults.suggestionChipBorder(
            enabled = isSelected,
            borderWidth = 1.dp,
            borderColor = Color.Black,
        ),
    )
}
