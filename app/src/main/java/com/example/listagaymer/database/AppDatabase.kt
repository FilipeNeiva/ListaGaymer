package com.example.listagaymer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.listagaymer.database.dao.GameDao
import com.example.listagaymer.database.dao.PlayerDao
import com.example.listagaymer.model.Game
import com.example.listagaymer.model.Player

@Database(
    entities = [
        Player::class,
        Game::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao

    abstract fun gameDao(): GameDao

    companion object {

        private lateinit var db: AppDatabase

        fun instance(context: Context): AppDatabase {

            if (::db.isInitialized) return db
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                ""
            ).build().also {
                db = it
            }
        }
    }
}