package com.soloscape.felt.presentations.felt.components

import java.time.ZonedDateTime

sealed class FeltEvent {
    data object DisplayAllDate : FeltEvent()
    data class FilterBySelectedDate(val dateTime: ZonedDateTime) : FeltEvent()
}
