package com.soloscape.database.data.repository.journal

import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.repository.JournalRepository
import com.soloscape.database.domain.repository.Journals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class FakeJournalRepositoryImpl : JournalRepository {

    private val journal = mutableMapOf<LocalDate, List<Journal>>()

    override fun getJournals(): Flow<Journals> = flow { emit(journal) }

    override fun getFilteredJournal(date: ZonedDateTime): Flow<Journals> {
        val startOfDayMillis = date.toLocalDate().atStartOfDay(date.zone).toEpochSecond() * 1000
        val endOfDayMillis = date.toLocalDate().plusDays(1).atStartOfDay(date.zone).toEpochSecond() * 1000

        return flow {
            val filtered = journal.filterKeys { localDate ->
                val localDateMillis = localDate.atStartOfDay(date.zone).toEpochSecond() * 1000
                localDateMillis in startOfDayMillis until endOfDayMillis
            }
            emit(filtered)
        }
    }

    override suspend fun getJournalById(id: Int): Journal? =
        journal.values.flatten().find { it.id == id }

    override suspend fun addJournal(write: Journal) {
        val date = Instant.ofEpochMilli(write.date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        journal.compute(date) { _, existing ->
            val updatedList = existing?.toMutableList() ?: mutableListOf()
            updatedList.add(write)
            updatedList
        }
    }

    override suspend fun deleteJournal(write: Journal) {
        val date = Instant.ofEpochMilli(write.date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        journal[date]?.let { existingList ->
            val updatedList = existingList.filterNot { it.id == write.id }.toMutableList()
            if (updatedList.isEmpty()) {
                journal.remove(date)
            } else {
                journal[date] = updatedList
            }
        }
    }

    override suspend fun deleteAllJournal() = journal.clear()
}
