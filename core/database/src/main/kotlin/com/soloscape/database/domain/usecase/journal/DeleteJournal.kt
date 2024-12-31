package com.soloscape.database.domain.usecase.journal

import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.repository.JournalRepository

class DeleteJournal(
    private val repository: JournalRepository,
) {
    suspend operator fun invoke(write: Journal) = repository.deleteJournal(write)
}
