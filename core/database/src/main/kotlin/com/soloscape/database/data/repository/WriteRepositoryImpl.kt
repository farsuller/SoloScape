package com.soloscape.database.data.repository


import com.soloscape.database.data.local.WriteDao
import com.soloscape.database.domain.model.Write
import com.soloscape.database.domain.repository.WriteRepository
import com.soloscape.database.domain.repository.Writes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

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
        val startOfDay = date.toLocalDate().atStartOfDay(date.zone).toEpochSecond()
        val endOfDay = date.toLocalDate().plusDays(1).atStartOfDay(date.zone).toEpochSecond()

        return dao.getWritesFiltered(startOfDay, endOfDay).map { writes ->
            writes.groupBy { write ->
                Instant.ofEpochMilli(write.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
        }
    }
}