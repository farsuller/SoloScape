package com.soloscape.idea.presentations.idea.components

import com.soloscape.database.domain.model.Note

sealed class IdeaEvent {
    data class DeleteNote(val note: Note) : IdeaEvent()
    data object RestoreNote : IdeaEvent()
}
