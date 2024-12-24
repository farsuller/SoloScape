package com.soloscape.database.domain.usecase.write

import com.soloscape.database.domain.model.Write
import com.soloscape.database.domain.repository.WriteRepository

class AddWrite(
    private val repository: WriteRepository,
) {
    suspend operator fun invoke(write: Write) = repository.addWrite(write)
}
