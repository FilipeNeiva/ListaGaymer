package com.example.listagaymer.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.listagaymer.model.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Insert
    suspend fun create(player: Player)

    @Query("SELECT * FROM Player WHERE username = :playerId")
    fun getPlayerById(playerId: String): Flow<Player>?
}