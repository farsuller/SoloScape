package com.soloscape.database.domain.journal

import com.google.common.truth.Truth.assertThat
import com.soloscape.database.data.repository.journal.FakeJournalRepositoryImpl
import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.usecase.journal.AddJournal
import com.soloscape.database.domain.usecase.journal.GetJournalByFiltered
import com.soloscape.database.domain.usecase.journal.GetJournalById
import com.soloscape.database.domain.usecase.journal.GetJournals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.ZonedDateTime

class GetJournalTest {

    private lateinit var getJournal: GetJournals
    private lateinit var getJournalById: GetJournalById
    private lateinit var getJournalByFiltered: GetJournalByFiltered
    private lateinit var addJournal: AddJournal
    private lateinit var fakeJournalRepository: FakeJournalRepositoryImpl

    @Before
    fun setUp() {
        fakeJournalRepository = FakeJournalRepositoryImpl()
        getJournal = GetJournals(fakeJournalRepository)
        getJournalById = GetJournalById(fakeJournalRepository)
        getJournalByFiltered = GetJournalByFiltered(fakeJournalRepository)
        addJournal = AddJournal(fakeJournalRepository)
    }

    @Test
    fun `Get journal by valid id, returns correct journal`() = runBlocking {
        val journal = Journal(
            id = 1,
            title = "Test Journal 1",
            content = "This is the first test journal",
            mood = "Happy",
            date = System.currentTimeMillis(),
        )

        addJournal.invoke(journal)

        val journalById = getJournalById(1)

        assertThat(journalById?.id).isNotNull()
        assertThat(journalById?.title).isEqualTo("Test Journal 1")
        assertThat(journalById?.content).isEqualTo("This is the first test journal")
        assertThat(journalById?.mood).isEqualTo("Happy")
        assertThat(journalById?.date).isNotEqualTo(0)
    }

    @Test
    fun `Get journal by invalid id, returns null`() = runBlocking {
        val journal = getJournalById(999) // Non-existent ID

        assertThat(journal).isNull()
    }

    @Test
    fun `Filter journals by date range, returns correct journals`() = runBlocking {
        val dateToFilter = ZonedDateTime.now()
        val journalToMatch = Journal(
            id = 3,
            title = "Filtered Journal",
            content = "This journal matches the filter",
            mood = "Calm",
            date = dateToFilter.toInstant().toEpochMilli(),
        )

        addJournal.invoke(journalToMatch)

        val filteredJournals = getJournalByFiltered(dateToFilter).first()
        val journalList = filteredJournals.values.flatten()

        assertThat(journalList).contains(journalToMatch)
    }

    @Test
    fun `Filter journals with no match in date range, returns empty map`() = runBlocking {
        val unrelatedDate = ZonedDateTime.now().minusDays(7) // Outside the filter range
        val unrelatedJournal = Journal(
            id = 4,
            title = "Unrelated Journal",
            content = "This journal does not match the filter",
            mood = "Sad",
            date = unrelatedDate.toInstant().toEpochMilli(),
        )

        addJournal.invoke(unrelatedJournal)

        val filteredJournals = getJournalByFiltered(ZonedDateTime.now()).first()
        val journalList = filteredJournals.values.flatten()

        assertThat(journalList).isEmpty()
    }

    @Test
    fun `Get journals when none exist, returns empty map`() = runBlocking {
        val allJournals = fakeJournalRepository.getJournals().first()
        assertThat(allJournals).isEmpty()
    }
}
