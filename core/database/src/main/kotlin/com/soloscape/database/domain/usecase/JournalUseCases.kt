package com.soloscape.database.domain.usecase

import com.soloscape.database.domain.usecase.journal.AddJournal
import com.soloscape.database.domain.usecase.journal.DeleteAllJournal
import com.soloscape.database.domain.usecase.journal.DeleteJournal
import com.soloscape.database.domain.usecase.journal.GetJournalByFiltered
import com.soloscape.database.domain.usecase.journal.GetJournalById
import com.soloscape.database.domain.usecase.journal.GetJournals

data class JournalUseCases(
    val getWrite: GetJournals,
    val getWriteById: GetJournalById,
    val getWriteByFiltered: GetJournalByFiltered,
    val addWrite: AddJournal,
    val deleteWrite: DeleteJournal,
    val deleteAllWrite: DeleteAllJournal,
)
