package com.soloscape.database.domain.repository

import com.soloscape.database.domain.model.Journal
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZonedDateTime

typealias Journals = Map<LocalDate, List<Journal>>

interface JournalRepository {

    fun getJournals(): Flow<Journals>

    fun getFilteredJournal(date: ZonedDateTime): Flow<Journals>

    suspend fun getJournalById(id: Int): Journal?

    suspend fun addJournal(write: Journal)

    suspend fun deleteJournal(write: Journal)

    suspend fun deleteAllJournal()
}
