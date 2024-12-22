package com.soloscape.home.presentations.write

import com.soloscape.database.domain.model.Write


sealed class WriteEvent {
    data class UpsertWriteItem(val writeItem: Write, val onSuccess: () -> Unit = {}) : WriteEvent()
    data class DeleteWriteItem(val writeItem: Write, val onSuccess: () -> Unit = {}) : WriteEvent()
    data object DeleteAllWriteItem : WriteEvent()
}
