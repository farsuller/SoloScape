package com.soloscape.database.domain.usecase

import com.soloscape.database.domain.model.Write
import com.soloscape.database.domain.repository.WriteRepository

class GetWriteById(
    private val repository: WriteRepository
) {
    suspend operator fun invoke(id: Int): Write? = repository.getWriteById(id)
}