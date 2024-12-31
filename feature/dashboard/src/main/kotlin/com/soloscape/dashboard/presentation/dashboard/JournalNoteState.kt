package com.soloscape.dashboard.presentation.dashboard

import com.soloscape.dashboard.model.JournalNoteItem
import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.model.Note

data class JournalNoteState(
    val notesList: List<Note>? = null,
    val journalList: List<Journal>? = null,
    val combinedList: List<JournalNoteItem>? = null,
    val errorMessage: String? = null,
)
