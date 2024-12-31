package com.soloscape.felt.presentations.write.components

import androidx.compose.ui.focus.FocusState
import com.soloscape.database.domain.model.Journal
import com.soloscape.ui.Reaction

sealed class WriteEvent {
    data class UpsertWriteItem(val reaction: Reaction, val onSuccess: () -> Unit = {}) : WriteEvent()
    data class DeleteWriteItem(val writeItem: Journal, val onSuccess: () -> Unit = {}) : WriteEvent()
    data class EnteredTitle(val value: String) : WriteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : WriteEvent()
    data class EnteredContent(val value: String) : WriteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : WriteEvent()
    data class SelectedDateTime(val date: Long?) : WriteEvent()
}
