package com.soloscape.home.presentations.write

import android.annotation.SuppressLint
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import com.soloscape.database.domain.model.Write
import com.soloscape.home.presentations.write.components.NoteTopBar
import com.soloscape.home.presentations.write.components.SaveButton
import com.soloscape.home.presentations.write.components.WriteChanges
import com.soloscape.ui.GalleryImage
import com.soloscape.ui.Mood
import com.soloscape.ui.components.SaveButton
import java.time.ZonedDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun NoteScreen(
    pagerState: PagerState,
    onDeleteConfirmed: (Write) -> Unit,
    onBackPressed: () -> Unit,
    uiState: WriteState,
    moodName: () -> String,
    onSaveClicked: () -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onValueChange : (String) -> Unit,
    onFocusChange : (FocusState) -> Unit,
    noteTitle : String,
    noteContent : String
) {

    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }

    Scaffold(
        topBar = {
            NoteTopBar(
                selectedWrite = uiState.selectedWrite,
                onDeleteConfirmed = onDeleteConfirmed,
                onBackPressed = onBackPressed,
                moodName = moodName,
                onDateTimeUpdated = onDateTimeUpdated,
            )
        },
        floatingActionButton = {
            SaveButton(
                onClick = onSaveClicked,
                color = MaterialTheme.colorScheme.tertiary,
            )
        },
        content = { paddingValues ->
            NoteContent(
                writeState = uiState,
                pagerState = pagerState,
                paddingValues = paddingValues,
                onValueChangeTitle= onValueChange,
                onFocusChangeTitle = onFocusChange,
                onValueChangeContent= onValueChange,
                onFocusChangeContent = onFocusChange,
                noteTitle = noteTitle,
                noteContent = noteContent
            )
        },
    )
}


