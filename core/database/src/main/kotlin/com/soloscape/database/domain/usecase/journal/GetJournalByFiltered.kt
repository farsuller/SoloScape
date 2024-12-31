package com.soloscape.database.domain.usecase.journal

import com.soloscape.database.domain.repository.JournalRepository
import com.soloscape.database.domain.repository.Journals
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

class GetJournalByFiltered(
    private val repository: JournalRepository,
) {
    operator fun invoke(date: ZonedDateTime): Flow<Journals> = repository.getFilteredJournal(date)
}
