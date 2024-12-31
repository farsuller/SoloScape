package com.soloscape.database.data.repository

import com.soloscape.database.data.local.JournalDao
import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.repository.JournalRepository
import com.soloscape.database.domain.repository.Journals
import com.soloscape.util.toLocalDateOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class JournalRepositoryImpl(private val dao: JournalDao) : JournalRepository {

    override fun getJournals(): Flow<Journals> = dao.getJournals().map { writes ->
        writes.groupBy { write ->
            Instant.ofEpochMilli(write.date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
    }

    override suspend fun getJournalById(id: Int): Journal? = dao.getJournalById(id)

    override suspend fun addJournal(write: Journal) = dao.addJournal(write)

    override suspend fun deleteJournal(write: Journal) = dao.deleteJournal(write)

    override suspend fun deleteAllJournal() = dao.deleteAllJournals()

    override fun getFilteredJournal(date: ZonedDateTime): Flow<Journals> {
        val startOfDayMillis = date.toLocalDate().atStartOfDay(date.zone).toEpochSecond() * 1000
        val endOfDayMillis = date.toLocalDate().plusDays(1).atStartOfDay(date.zone).toEpochSecond() * 1000

        return dao.getWritesFiltered(startOfDayMillis, endOfDayMillis)
            .map { writes ->
                writes.groupBy { write ->
                    write.date.toLocalDateOrNull() ?: LocalDate.MIN
                }
            }
            .catch {
                emit(emptyMap())
            }
    }
}
