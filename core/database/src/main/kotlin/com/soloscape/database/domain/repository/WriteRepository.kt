package com.soloscape.database.domain.repository

import com.soloscape.database.domain.model.Write
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZonedDateTime

typealias Writes = Map<LocalDate, List<Write>>

interface WriteRepository {

    fun getWrites(): Flow<Writes>
    fun getFilteredWrite(date: ZonedDateTime): Flow<Writes>
    suspend fun getWriteById(id: Int): Write?
    suspend fun addWrite(write: Write)
    suspend fun deleteWrite(write: Write)
    suspend fun deleteAllWrite()
}
