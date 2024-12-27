package com.soloscape.idea.presentations.note.components

import androidx.compose.ui.graphics.toArgb
import com.soloscape.database.domain.model.Note

data class NoteState(
    val id: Int? = null,
    val title: String = "",
    val titleHint: String = "Enter title...",
    val titleHintVisible: Boolean = true,
    val content: String = "",
    val contentHint: String = "Enter some content...",
    val contentHintVisible: Boolean = true,
    val noteColor: Int = Note.noteColors.random().toArgb(),
)
