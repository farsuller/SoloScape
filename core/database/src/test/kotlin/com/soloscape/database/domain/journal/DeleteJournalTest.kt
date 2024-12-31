package com.soloscape.database.domain.journal

import com.google.common.truth.Truth.assertThat
import com.soloscape.database.data.repository.journal.FakeJournalRepositoryImpl
import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.usecase.journal.DeleteJournal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteJournalTest {

    private lateinit var deleteJournal: DeleteJournal
    private lateinit var fakeJournalRepository: FakeJournalRepositoryImpl

    @Before
    fun setUp() {
        fakeJournalRepository = FakeJournalRepositoryImpl()
        deleteJournal = DeleteJournal(fakeJournalRepository)

        runBlocking {
            fakeJournalRepository.addJournal(
                Journal(
                    id = 1,
                    title = "Test Journal 1",
                    content = "This is the first test journal",
                    mood = "Happy",
                    date = System.currentTimeMillis(),
                ),
            )
            fakeJournalRepository.addJournal(
                Journal(
                    id = 2,
                    title = "Test Journal 2",
                    content = "This is the second test journal",
                    mood = "Happy",
                    date = System.currentTimeMillis(),
                ),
            )
        }
    }

    @Test
    fun `Delete existing journal, journal is removed from repository`() = runBlocking {
        val journalToDelete = Journal(
            id = 1,
            title = "Test Journal 1",
            content = "This is the first test journal",
            mood = "Happy",
            date = System.currentTimeMillis(),
        )

        deleteJournal.invoke(journalToDelete)

        val allJournals = fakeJournalRepository.getJournals().first()
        val journalList = allJournals.values.flatten()
        assertThat(journalList).doesNotContain(journalToDelete)
    }

    @Test
    fun `Delete non-existent journal, repository remains unchanged`() = runBlocking {
        val nonExistentNote = Journal(
            id = 999,
            title = "Non-Existent Note",
            content = "This note doesn't exist",
            mood = "This note doesn't exist",
            date = System.currentTimeMillis(),
        )

        val allJournalsBefore = fakeJournalRepository.getJournals().first()
        val journalListBefore = allJournalsBefore.values.flatten()

        deleteJournal.invoke(nonExistentNote)

        val allJournalsAfter = fakeJournalRepository.getJournals().first()
        val journalListAfter = allJournalsAfter.values.flatten()

        assertThat(journalListBefore).isEqualTo(journalListAfter)
    }
}
