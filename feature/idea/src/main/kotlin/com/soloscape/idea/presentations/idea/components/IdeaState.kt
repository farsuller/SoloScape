package com.soloscape.idea.presentations.idea.components

import com.soloscape.database.domain.model.Note
import com.soloscape.util.NoteOrder
import com.soloscape.util.OrderType

data class IdeaState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
)
