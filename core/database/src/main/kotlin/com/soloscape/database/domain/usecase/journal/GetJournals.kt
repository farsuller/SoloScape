package com.soloscape.database.domain.usecase.journal

import com.soloscape.database.domain.repository.JournalRepository
import com.soloscape.database.domain.repository.Journals
import kotlinx.coroutines.flow.Flow

class GetJournals(
    private val repository: JournalRepository,
) {
    operator fun invoke(): Flow<Journals> = repository.getJournals()
}
