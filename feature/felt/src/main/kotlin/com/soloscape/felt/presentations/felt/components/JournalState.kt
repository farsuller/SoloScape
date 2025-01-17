package com.soloscape.felt.presentations.felt.components

import com.soloscape.database.domain.model.Journal
import java.time.LocalDate

data class JournalState(
    val writes: Map<LocalDate, List<Journal>> = emptyMap(),
    val errorMessage: String? = null,
)
