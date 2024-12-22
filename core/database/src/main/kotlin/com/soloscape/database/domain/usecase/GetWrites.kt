package com.soloscape.database.domain.usecase

import com.soloscape.database.domain.repository.WriteRepository
import com.soloscape.database.domain.repository.Writes
import kotlinx.coroutines.flow.Flow

class GetWrites (
    private val repository: WriteRepository
) {
    operator fun invoke(): Flow<Writes> = repository.getWrites()
}