package com.example.listagaymer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val rate: Int?,
    val playerId: String
)
