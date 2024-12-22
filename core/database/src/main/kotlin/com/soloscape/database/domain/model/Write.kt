package com.soloscape.database.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Write(

    @PrimaryKey
    val id: Int = 0,
    var mood: String? = null,
    val title: String? = null,
    val description: String? = null,
    val date: Long = 0, // Store as epoch milliseconds
)
