package com.example.listagaymer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val status: String,
    val rate: Int?,
    val playerId: String
)
