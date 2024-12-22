package com.soloscape.home.presentations.write

import com.soloscape.database.domain.model.Write
import com.soloscape.ui.Mood

data class WriteState(
    val selectedReportId: String? = null,
    val selectedReport: Write? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: Long = 0,
)
