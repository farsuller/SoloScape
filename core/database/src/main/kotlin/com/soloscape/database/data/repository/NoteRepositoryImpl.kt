package com.soloscape.database.data.repository

import com.soloscape.database.data.local.NoteDao
import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao,
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> = dao.getNotes()

    override suspend fun getNoteById(id: Int): Note? = dao.getNoteById(id)

    override suspend fun insertNote(note: Note) = dao.insertNote(note)

    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)
}
