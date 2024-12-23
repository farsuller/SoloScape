package com.soloscape.database.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Write(

    @PrimaryKey
    val id: Int? = null,
    var mood: String? = null,
    val title: String = "",
    val content: String = "",
    val date: Long = 0,
)
