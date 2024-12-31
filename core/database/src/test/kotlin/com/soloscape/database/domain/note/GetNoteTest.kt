package com.soloscape.database.domain.note

import com.google.common.truth.Truth.assertThat
import com.soloscape.database.data.repository.note.FakeNoteRepositoryImpl
import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.usecase.note.GetNote
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNoteTest {

    private lateinit var getNote: GetNote
    private lateinit var fakeNoteRepository: FakeNoteRepositoryImpl

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepositoryImpl()
        getNote = GetNote(fakeNoteRepository)

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
    fun `Get note by valid id, returns correct note`() = runBlocking {
        val note = getNote(1)

        assertThat(note).isNotNull()
        assertThat(note?.title).isEqualTo("Note 1")
        assertThat(note?.content).isEqualTo("Content 1")
        assertThat(note?.color).isEqualTo(1)
    }

    @Test
    fun `Get note by invalid id, returns null`() = runBlocking {
        val note = getNote(999) // Assuming no note with this ID

        assertThat(note).isNull()
    }

    @Test
    fun `Get note from empty repository, returns null`() = runBlocking {
        fakeNoteRepository.clear()

        val note = getNote(1)

        assertThat(note).isNull()
    }
}
