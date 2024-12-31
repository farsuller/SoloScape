package com.soloscape.database.domain.usecase.journal

import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.repository.JournalRepository

class GetJournalById(
    private val repository: JournalRepository,
) {
    suspend operator fun invoke(id: Int): Journal? = repository.getJournalById(id)
}
