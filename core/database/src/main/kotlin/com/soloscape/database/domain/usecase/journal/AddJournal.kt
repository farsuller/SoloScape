package com.soloscape.database.domain.usecase.journal

import com.soloscape.database.domain.model.InvalidJournalException
import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.repository.JournalRepository

class AddJournal(
    private val repository: JournalRepository,
) {
    @Throws(InvalidJournalException::class)
    suspend operator fun invoke(write: Journal) {
        if (write.title.isBlank()) {
            throw InvalidJournalException("The title of the journal can't be empty")
        }
        if (write.content.isBlank()) {
            throw InvalidJournalException("The content of the journal can't be empty")
        }

        repository.addJournal(write)
    }
}
