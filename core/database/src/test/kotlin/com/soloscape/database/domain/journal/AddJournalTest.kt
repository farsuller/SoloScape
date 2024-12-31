package com.soloscape.database.domain.journal

import com.google.common.truth.Truth.assertThat
import com.soloscape.database.data.repository.journal.FakeJournalRepositoryImpl
import com.soloscape.database.domain.model.InvalidJournalException
import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.usecase.journal.AddJournal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class AddJournalTest {

    private lateinit var addJournal: AddJournal
    private lateinit var fakeJournalRepository: FakeJournalRepositoryImpl

    @Before
    fun setUp() {
        fakeJournalRepository = FakeJournalRepositoryImpl()
        addJournal = AddJournal(fakeJournalRepository)
    }

    @Test
    fun `Add valid journal,journal is added`(): Unit = runBlocking {
        // Arrange
        val journal1 = Journal(
            id = 1,
            title = "Test Journal 1",
            content = "This is the first test journal",
            mood = "Happy",
            date = System.currentTimeMillis(),
        )

        val journal2 = Journal(
            id = 1,
            title = "Test Journal 1",
            content = "This is the first test journal",
            mood = "Happy",
            date = System.currentTimeMillis(),
        )

        addJournal.invoke(journal1)
        addJournal.invoke(journal2)

        val allJournals = fakeJournalRepository.getJournals().first()
        val journalList = allJournals.values.flatten()
        assertThat(journalList).containsExactly(journal1, journal2)
    }

    @Test
    fun `Add journal with empty title, throws InvalidJournalException`() = runBlocking {
        // Arrange
        val journal = Journal(
            id = 1,
            title = "",
            content = "This is the first test journal",
            mood = "Happy",
            date = System.currentTimeMillis(),
        )

        val exception = assertThrows(InvalidJournalException::class.java) {
            runBlocking { addJournal.invoke(journal) }
        }

        assertThat(exception).hasMessageThat().isEqualTo("The title of the journal can't be empty")
    }

    @Test
    fun `Add journal with empty content, throws InvalidJournalException`() = runBlocking {
        // Arrange
        val journal = Journal(
            id = 1,
            title = "Test Journal 1",
            content = "",
            mood = "Happy",
            date = System.currentTimeMillis(),
        )

        val exception = assertThrows(InvalidJournalException::class.java) {
            runBlocking { addJournal.invoke(journal) }
        }

        assertThat(exception).hasMessageThat()
            .isEqualTo("The content of the journal can't be empty")
    }

    @Test
    fun `Add journal with valid title, content and reaction, repository is updated`() =
        runBlocking {
            // Arrange
            val journal = Journal(
                id = 1,
                title = "Valid Title",
                content = "Valid Content",
                mood = "Happy",
                date = System.currentTimeMillis(),
            )

            // Act
            addJournal.invoke(journal)

            // Assert
            val allJournals = fakeJournalRepository.getJournals().first()

            // Flatten the map to a list of journals
            val journalList = allJournals.values.flatten()

            // Check that there is exactly one journal
            assertThat(journalList.size).isEqualTo(1)

            // Retrieve the added journal
            val addedJournal = journalList.first()

            // Assert the properties of the added journal
            assertThat(addedJournal.title).isEqualTo("Valid Title")
            assertThat(addedJournal.content).isEqualTo("Valid Content")
            assertThat(addedJournal.mood).isEqualTo("Happy")
            assertThat(addedJournal.date).isEqualTo(journal.date)
        }
}
