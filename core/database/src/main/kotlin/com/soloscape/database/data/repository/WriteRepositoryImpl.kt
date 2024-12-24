package com.soloscape.database.data.repository

import com.soloscape.database.data.local.WriteDao
import com.soloscape.database.domain.model.Write
import com.soloscape.database.domain.repository.WriteRepository
import com.soloscape.database.domain.repository.Writes
import com.soloscape.util.toLocalDateOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class WriteRepositoryImpl(private val dao: WriteDao) : WriteRepository {
    override fun getWrites(): Flow<Writes> {
        return dao.getWrites().map { writes ->
            writes.groupBy { write ->
                Instant.ofEpochMilli(write.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
        }
    }

    override suspend fun getWriteById(id: Int): Write? {
        return dao.getWriteById(id)
    }

    override suspend fun addWrite(write: Write) {
        dao.addWrite(write)
    }

    override suspend fun deleteWrite(write: Write) {
        dao.deleteWrite(write)
    }

    override suspend fun deleteAllWrite() {
        dao.deleteAllWrites()
    }

    override fun getFilteredWrite(date: ZonedDateTime): Flow<Writes> {
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
