package com.soloscape.home.presentations.home.components

import com.soloscape.database.domain.model.Write
import java.time.LocalDate

data class HomeState(
    val writes : Map<LocalDate, List<Write>>? = null,
    val errorMessage : String? = null
)
