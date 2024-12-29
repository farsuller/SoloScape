package com.soloscape.idea.presentations.idea.components

import com.soloscape.database.domain.model.Note

data class IdeaState(
    val notes: List<Note> = emptyList(),
)
