package com.soloscape.idea.presentations.note.components

import androidx.compose.ui.focus.FocusState
import com.soloscape.database.domain.model.Note

sealed class NoteEvent {
    data class EnteredTitle(val value: String) : NoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : NoteEvent()
    data class EnteredContent(val value: String) : NoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : NoteEvent()
    data class ChangeColor(val color: Int) : NoteEvent()
    data class SaveNote(val onSuccess: () -> Unit = {}) : NoteEvent()
    data class DeleteNoteItem(val noteItem: Note, val onSuccess: () -> Unit = {}) : NoteEvent()
}
