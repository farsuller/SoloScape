package com.soloscape.note.model

import com.soloscape.util.model.Mood
import com.soloscape.util.model.Report
import io.realm.kotlin.types.RealmInstant

data class UiNoteState(
    val selectedReportId: String? = null,
    val selectedReport: Report? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: RealmInstant? = null,
)
