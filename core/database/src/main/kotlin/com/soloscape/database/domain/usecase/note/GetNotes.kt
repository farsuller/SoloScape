package com.soloscape.database.domain.usecase.note

import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotes(
    private val repository: NoteRepository,
) {
    operator fun invoke(): Flow<List<Note>> = repository.getNotes()
}
