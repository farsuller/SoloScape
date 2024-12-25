package com.soloscape.database.domain.usecase.note

import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}