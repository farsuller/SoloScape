package com.soloscape.database.domain.usecase.note

import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.repository.NoteRepository

class GetNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? = repository.getNoteById(id)
}