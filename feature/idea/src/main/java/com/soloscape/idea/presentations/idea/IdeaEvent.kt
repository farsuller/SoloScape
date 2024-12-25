package com.soloscape.idea.presentations.idea

import com.soloscape.database.domain.model.Note
import com.soloscape.util.NoteOrder

sealed class IdeaEvent {
    data class Order(val noteOrder: NoteOrder) : IdeaEvent()
    data class DeleteNote(val note: Note) : IdeaEvent()
    data object RestoreNote : IdeaEvent()
    data object ToggleOrderSection : IdeaEvent()
}