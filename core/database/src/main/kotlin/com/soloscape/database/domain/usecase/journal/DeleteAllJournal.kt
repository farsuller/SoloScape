package com.soloscape.database.domain.usecase.journal

import com.soloscape.database.domain.repository.JournalRepository

class DeleteAllJournal(
    private val repository: JournalRepository,
) {
    suspend operator fun invoke() = repository.deleteAllJournal()
}
