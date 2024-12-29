package com.soloscape.util

sealed class OrderType {
    data object Ascending : OrderType()
    data object Descending : OrderType()
}
