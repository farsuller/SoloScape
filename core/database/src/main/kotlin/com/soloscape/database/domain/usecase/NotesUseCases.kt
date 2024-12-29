package com.soloscape.database.domain.usecase

import com.soloscape.database.domain.usecase.note.AddNote
import com.soloscape.database.domain.usecase.note.DeleteNote
import com.soloscape.database.domain.usecase.note.GetNote
import com.soloscape.database.domain.usecase.note.GetNotes

data class NotesUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote,
)
