package com.soloscape.dashboard.presentation.dashboard

import com.soloscape.dashboard.model.JournalNoteItem
import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.model.Write

data class JournalNoteState(
    val notesList: List<Note>? = null,
    val journalList: List<Write>? = null,
    val combinedList: List<JournalNoteItem>? = null,
    val errorMessage: String? = null,
)
