package com.soloscape.felt.presentations.write

import android.annotation.SuppressLint
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusState
import com.soloscape.database.domain.model.Journal
import com.soloscape.felt.presentations.write.components.WriteState
import com.soloscape.felt.presentations.write.components.WriteTopBar
import com.soloscape.ui.Reaction
import com.soloscape.ui.components.SaveButton
import java.time.ZonedDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun WriteScreen(
    pagerState: PagerState,
    onDeleteConfirmed: (Journal) -> Unit,
    onBackPressed: () -> Unit,
    reaction: Reaction,
    onSaveClicked: () -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onValueChangeTitle: (String) -> Unit,
    onFocusChangeTitle: (FocusState) -> Unit,
    onValueChangeContent: (String) -> Unit,
    onFocusChangeContent: (FocusState) -> Unit,
    writeState: WriteState,
) {
    LaunchedEffect(key1 = writeState.reaction) {
        pagerState.scrollToPage(Reaction.valueOf(writeState.reaction.name).ordinal)
    }

    Scaffold(
        topBar = {
            WriteTopBar(
                selectedWrite = writeState.writeItem,
                onDeleteConfirmed = onDeleteConfirmed,
                onBackPressed = onBackPressed,
                reaction = reaction,
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
            WriteContent(
                pagerState = pagerState,
                paddingValues = paddingValues,
                onValueChangeTitle = onValueChangeTitle,
                onFocusChangeTitle = onFocusChangeTitle,
                onValueChangeContent = onValueChangeContent,
                onFocusChangeContent = onFocusChangeContent,
                writeState = writeState,
            )
        },
    )
}
