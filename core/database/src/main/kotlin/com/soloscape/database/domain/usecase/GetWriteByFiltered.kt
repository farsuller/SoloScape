package com.soloscape.database.domain.usecase

import com.soloscape.database.domain.repository.WriteRepository
import com.soloscape.database.domain.repository.Writes
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

class GetWriteByFiltered(
    private val repository: WriteRepository
) {
    operator fun invoke(date: ZonedDateTime): Flow<Writes> = repository.getFilteredWrite(date)
}