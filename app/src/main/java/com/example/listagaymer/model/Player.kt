package com.example.listagaymer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey
    val username: String,
    val email: String,
    val password: String
)
