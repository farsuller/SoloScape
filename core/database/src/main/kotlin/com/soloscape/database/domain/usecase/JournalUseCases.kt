package com.soloscape.database.domain.usecase

import com.soloscape.database.domain.usecase.journal.AddJournal
import com.soloscape.database.domain.usecase.journal.DeleteAllJournal
import com.soloscape.database.domain.usecase.journal.DeleteJournal
import com.soloscape.database.domain.usecase.journal.GetJournalByFiltered
import com.soloscape.database.domain.usecase.journal.GetJournalById
import com.soloscape.database.domain.usecase.journal.GetJournals

data class JournalUseCases(
    val getJournals: GetJournals,
    val getJournalById: GetJournalById,
    val getJournalByFiltered: GetJournalByFiltered,
    val addJournal: AddJournal,
    val deleteJournal: DeleteJournal,
    val deleteAllJournal: DeleteAllJournal,
)
