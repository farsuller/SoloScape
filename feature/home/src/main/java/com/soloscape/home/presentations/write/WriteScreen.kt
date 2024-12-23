package com.soloscape.home.presentations.write

import android.annotation.SuppressLint
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.painterResource
import com.soloscape.database.domain.model.Write
import com.soloscape.home.presentations.write.components.NoteTopBar
import com.soloscape.home.presentations.write.components.SaveButton
import com.soloscape.home.presentations.write.components.WriteTextFieldState
import java.time.ZonedDateTime
import com.soloscape.ui.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun NoteScreen(
    pagerState: PagerState,
    onDeleteConfirmed: (Write) -> Unit,
    onBackPressed: () -> Unit,
    uiState: WriteTextFieldState,
    moodName: () -> String,
    onSaveClicked: () -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onValueChangeTitle: (String) -> Unit,
    onFocusChangeTitle: (FocusState) -> Unit,
    onValueChangeContent: (String) -> Unit,
    onFocusChangeContent: (FocusState) -> Unit,
    noteTitleState : State<WriteTextFieldState>,
    noteContentState : State<WriteTextFieldState>
) {

//    LaunchedEffect(key1 = uiState.mood) {
//        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
//    }

    Scaffold(
        topBar = {
            NoteTopBar(
                onDeleteConfirmed = onDeleteConfirmed,
                onBackPressed = onBackPressed,
                moodName = moodName,
                onDateTimeUpdated = onDateTimeUpdated,
            )
        },
        floatingActionButton = {
            SaveButton(
                onClick = onSaveClicked,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        content = { paddingValues ->
            NoteContent(
                writeState = uiState,
                pagerState = pagerState,
                paddingValues = paddingValues,
                onValueChangeTitle = onValueChangeTitle,
                onFocusChangeTitle = onFocusChangeTitle,
                onValueChangeContent = onValueChangeContent,
                onFocusChangeContent = onFocusChangeContent,
                noteTitleState = noteTitleState,
                noteContentState = noteContentState
            )
        },
    )
}


