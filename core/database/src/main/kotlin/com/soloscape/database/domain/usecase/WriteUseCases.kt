package com.soloscape.database.domain.usecase

data class WriteUseCases (
    val getWrite: GetWrites,
    val getWriteById: GetWriteById,
    val getWriteByFiltered: GetWriteByFiltered,
    val deleteWrite: DeleteWrite,
    val addWrite: AddWrite,
)