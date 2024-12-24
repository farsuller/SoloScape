package com.soloscape.database.domain.usecase.write

import com.soloscape.database.domain.repository.WriteRepository

class DeleteAllWrite(
    private val repository: WriteRepository,
) {
    suspend operator fun invoke() = repository.deleteAllWrite()
}
