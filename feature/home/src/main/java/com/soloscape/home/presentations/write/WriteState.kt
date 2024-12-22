package com.soloscape.home.presentations.write

import com.soloscape.database.domain.model.Write
import com.soloscape.ui.Mood

data class WriteState(
    val selectedWrite: Write? = null,
    val title: String = "",
    val content: String = "",
    val hint : String = "",
    val isHintVisible : Boolean = true,
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: Long = 0,
)
