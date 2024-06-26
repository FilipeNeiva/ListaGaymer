package com.example.listagaymer.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.listagaymer.model.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert
    suspend fun create(game: Game)

    @Query("SELECT * FROM Game WHERE playerId = :playerId AND status = :status")
    fun getGames(playerId: String, status: String): Flow<List<Game>>

    @Delete
    suspend fun remove(game: Game)
}