package com.soloscape.home.presentations.write.components

import androidx.compose.ui.focus.FocusState
import com.soloscape.database.domain.model.Write

sealed class WriteEvent {
    data class UpsertWriteItem(val onSuccess: () -> Unit = {}) : WriteEvent()
    data class DeleteWriteItem(val writeItem: Write, val onSuccess: () -> Unit = {}) : WriteEvent()
    data object DeleteAllWriteItem : WriteEvent()
    data class EnteredTitle(val value: String) : WriteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : WriteEvent()
    data class EnteredContent(val value: String) : WriteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : WriteEvent()
    data class SelectedDateTime(val date: Long?) : WriteEvent()
}
