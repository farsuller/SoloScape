package com.soloscape.database.domain.usecase

import com.soloscape.database.domain.usecase.write.AddWrite
import com.soloscape.database.domain.usecase.write.DeleteAllWrite
import com.soloscape.database.domain.usecase.write.DeleteWrite
import com.soloscape.database.domain.usecase.write.GetWriteByFiltered
import com.soloscape.database.domain.usecase.write.GetWriteById
import com.soloscape.database.domain.usecase.write.GetWrites

data class WriteUseCases(
    val getWrite: GetWrites,
    val getWriteById: GetWriteById,
    val getWriteByFiltered: GetWriteByFiltered,
    val addWrite: AddWrite,
    val deleteWrite: DeleteWrite,
    val deleteAllWrite: DeleteAllWrite,
)
