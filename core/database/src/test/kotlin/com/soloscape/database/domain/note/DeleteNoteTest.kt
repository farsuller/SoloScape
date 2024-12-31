package com.soloscape.database.domain.note

import com.google.common.truth.Truth.assertThat
import com.soloscape.database.data.repository.note.FakeNoteRepositoryImpl
import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.usecase.note.DeleteNote
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteNoteTest {

    private lateinit var deleteNote: DeleteNote
    private lateinit var fakeNoteRepository: FakeNoteRepositoryImpl

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepositoryImpl()
        deleteNote = DeleteNote(fakeNoteRepository)

        // Insert some sample notes in the fake repository
        runBlocking {
            fakeNoteRepository.insertNote(
                Note(
                    id = 1,
                    title = "Note 1",
                    content = "Content 1",
                    timestamp = System.currentTimeMillis(),
                    color = 1,
                ),
            )
            fakeNoteRepository.insertNote(
                Note(
                    id = 2,
                    title = "Note 2",
                    content = "Content 2",
                    timestamp = System.currentTimeMillis(),
                    color = 2,
                ),
            )
        }
    }

    @Test
    fun `Delete existing note, note is removed from repository`() = runBlocking {
        val noteToDelete = Note(
            id = 1,
            title = "Note 1",
            content = "Content 1",
            timestamp = System.currentTimeMillis(),
            color = 1,
        )

        deleteNote.invoke(noteToDelete)

        val allNotes = fakeNoteRepository.getNotes().first()
        assertThat(allNotes).doesNotContain(noteToDelete)
    }

    @Test
    fun `Delete non-existent note, repository remains unchanged`() = runBlocking {
        val nonExistentNote = Note(
            id = 999,
            title = "Non-Existent Note",
            content = "This note doesn't exist",
            timestamp = System.currentTimeMillis(),
            color = 3,
        )

        // Get the notes before trying to delete
        val notesBefore = fakeNoteRepository.getNotes().first()

        deleteNote.invoke(nonExistentNote)

        val notesAfter = fakeNoteRepository.getNotes().first()
        assertThat(notesBefore).isEqualTo(notesAfter)
    }
}
