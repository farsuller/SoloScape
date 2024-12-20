package com.soloscape.note

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.soloscape.note.model.NoteChanges
import com.soloscape.note.model.UiNoteState
import com.soloscape.ui.GalleryImage
import com.soloscape.ui.GalleryState
import com.soloscape.util.GalleryUploader
import com.soloscape.util.model.Mood
import com.soloscape.util.model.Report
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NoteContent(
    uiState: UiNoteState,
    noteChanges: NoteChanges,
    onNoteChange: (NoteChanges) -> Unit,
    pagerState: PagerState,
    paddingValues: PaddingValues,
    onSaveClicked: (Report) -> Unit,
    galleryState: GalleryState,
    onImageSelect: (Uri) -> Unit,
    onImageClicked: (GalleryImage) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val titleEmpty = uiState.title.isEmpty()
    val descriptionEmpty = uiState.description.isEmpty()

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = scrollState),
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            HorizontalPager(state = pagerState) { page ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        modifier = Modifier.size(120.dp),
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(Mood.values()[page].icon)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Mood Image",
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = noteChanges.title,
                onValueChange = {
                    onNoteChange(
                        NoteChanges(
                            title = it,
                            description = uiState.description,
                        ),
                    )
                },
                placeholder = {
                    Text(
                        text = "Title",
                        color = MaterialTheme.colorScheme.secondary,
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    errorBorderColor = MaterialTheme.colorScheme.secondary,
                    errorLabelColor = MaterialTheme.colorScheme.onSurface,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        scope.launch {
                            scrollState.animateScrollTo(Int.MAX_VALUE)
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    },
                ),
                maxLines = 1,
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = noteChanges.description,
                onValueChange = {
                    onNoteChange(
                        NoteChanges(
                            title = uiState.title,
                            description = it,
                        ),
                    )
                },
                placeholder = {
                    Text(
                        text = "How was your day?",
                        color = MaterialTheme.colorScheme.secondary,
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    errorBorderColor = MaterialTheme.colorScheme.secondary,
                    errorLabelColor = MaterialTheme.colorScheme.onSurface,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                    },
                ),
            )
        }

        Column(
            verticalArrangement = Arrangement.Bottom,
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            GalleryUploader(
                galleryState = galleryState,
                onAddClicked = {
                    focusManager.clearFocus()
                },
                onImageSelect = onImageSelect,
                onImageClicked = onImageClicked,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                onClick = {
                    when {
                        !titleEmpty && !descriptionEmpty -> {
                            onSaveClicked(
                                Report().apply {
                                    this.title = uiState.title
                                    this.description = uiState.description
                                    this.images =
                                        galleryState.images.map { it.remoteImagePath }.toRealmList()
                                },
                            )
                        }

                        titleEmpty && !descriptionEmpty -> Toast.makeText(
                            context,
                            "Title cannot be empty.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        !titleEmpty && descriptionEmpty -> Toast.makeText(
                            context,
                            "Description cannot be empty.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        else -> Toast.makeText(
                            context,
                            "Fields cannot be empty.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                },
                shape = Shapes().small,
            ) {
                Text(text = "Save")
            }
        }
    }
}
