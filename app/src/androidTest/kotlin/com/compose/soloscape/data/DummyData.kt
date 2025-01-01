package com.compose.soloscape.data

import androidx.compose.ui.graphics.toArgb
import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.model.Note.Companion.noteColors
import java.time.LocalDate

val dummyNotes = listOf(
    Note(
        id = 1,
        title = "Note 1",
        content = "Note Content 1",
        timestamp = LocalDate.of(2025, 1, 1).toEpochDay(),
        color = noteColors.random().toArgb(),
    ),
    Note(
        id = 2,
        title = "Note 2",
        content = "Note Content 2",
        timestamp = LocalDate.of(2025, 1, 1).toEpochDay(),
        color = noteColors.random().toArgb(),
    ),
    Note(
        id = 3,
        title = "Note 3",
        content = "Note Content 3",
        timestamp = LocalDate.of(2025, 1, 1).toEpochDay(),
        color = noteColors.random().toArgb(),
    ),
)

val dummyJournals = listOf(
    Journal(
        id = 1,
        mood = "Happy",
        title = "A Wonderful Day",
        content = "Today was an amazing day! I went hiking and enjoyed the fresh air.",
        date = LocalDate.of(2025, 1, 1).toEpochDay(),
    ),
    Journal(
        id = 2,
        mood = "Calm",
        title = "Reflections",
        content = "Spent some time thinking about the year ahead and setting goals.",
        date = LocalDate.of(2025, 1, 2).toEpochDay(),
    ),
    Journal(
        id = 3,
        mood = "Neutral",
        title = "New Beginnings",
        content = "Started a new project today, and I can't wait to see where it leads!",
        date = LocalDate.of(2025, 1, 3).toEpochDay(),
    ),
)

val journals = mapOf(
    LocalDate.of(2025, 1, 1) to listOf(dummyJournals[0]),
    LocalDate.of(2025, 1, 2) to listOf(dummyJournals[1]),
    LocalDate.of(2025, 1, 3) to listOf(dummyJournals[2]),
)
