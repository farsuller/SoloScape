package com.soloscape.felt.presentations.felt.components

import com.soloscape.database.domain.model.Write
import java.time.LocalDate

data class FeltState(
    val writes: Map<LocalDate, List<Write>>? = null,
    val errorMessage: String? = null,
)
