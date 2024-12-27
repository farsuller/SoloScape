package com.soloscape.felt.presentations.write.components

import com.soloscape.database.domain.model.Write
import com.soloscape.ui.Reaction

data class WriteState(
    val id: Int? = null,
    val title: String = "",
    val titleHint: String = "Enter title...",
    val titleHintVisible: Boolean = true,
    val content: String = "",
    val contentHint: String = "Enter some content...",
    val contentHintVisible: Boolean = true,
    val reaction: Reaction = Reaction.Neutral,
    val date: Long = System.currentTimeMillis(),
    val writeItem: Write? = null
)
