package com.soloscape.home.presentations.write

import android.annotation.SuppressLint
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.soloscape.database.domain.model.Write
import com.soloscape.home.presentations.write.components.NoteTopBar
import com.soloscape.home.presentations.write.components.WriteChanges
import com.soloscape.ui.GalleryImage
import com.soloscape.ui.Mood
import java.time.ZonedDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun NoteScreen(
    pagerState: PagerState,
    onDeleteConfirmed: (Write) -> Unit,
    onBackPressed: () -> Unit,
    uiState: WriteState,
    moodName: () -> String,
    onSaveClicked: (Write) -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
) {

    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }

    Scaffold(
        topBar = {
            NoteTopBar(
                selectedWrite = uiState.selectedReport,
                onDeleteConfirmed = onDeleteConfirmed,
                onBackPressed = onBackPressed,
                moodName = moodName,
                onDateTimeUpdated = onDateTimeUpdated,
            )
        },
        content = { paddingValues ->
            NoteContent(
                uiState = uiState,
                pagerState = pagerState,
                paddingValues = paddingValues,
                onSaveClicked = onSaveClicked,
            )
        },
    )
}


