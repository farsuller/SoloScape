package com.soloscape.felt.presentations.felt.components

import java.time.ZonedDateTime

sealed class JournalEvent {
    data object DisplayAllDate : JournalEvent()
    data class FilterBySelectedDate(val dateTime: ZonedDateTime) : JournalEvent()
    data object DeleteAll : JournalEvent()
}
