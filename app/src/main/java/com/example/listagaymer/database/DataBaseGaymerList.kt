package com.example.listagaymer.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.listagaymer.data.Game
import com.example.listagaymer.data.User

class DataBaseGaymerList(context: Context): SQLiteOpenHelper(context, DataBaseGaymerList.DB_NAME, null, DataBaseGaymerList.DB_VERSION) {
    companion object {
        private const val DB_VERSION = 2
        private val DB_NAME = "GAYMERLIST"
    }

    override fun onCreate(db: SQLiteDatabase?) = onUpgrade(db, 0, DB_VERSION)

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 0 || (oldVersion < 1 && newVersion == 1)) {
            db?.execSQL("CREATE TABLE IF NOT EXISTS USER (USERNAME TEXT PRIMARY KEY, EMAIL TEXT, SENHA TEXT)")
        }
        if (oldVersion == 0 || (oldVersion < 2 && newVersion == 2)) {
            db?.execSQL("CREATE TABLE IF NOT EXISTS GAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, STATUS TEXT, RATE INT, USER TEXT REFERENCES USER)")
        }
    }

    fun addUser(user: User): User {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put("USERNAME", user.username)
            put("EMAIL", user.email)
            put("SENHA", user.senha)
        }

        db.insertOrThrow("USER", null, values)
        db.close()

        return user

    }

    fun getUser(username: String): User? {
        val db = this.readableDatabase
        var users : Array<User> = emptyArray()


        val cursor = db.rawQuery("""
            SELECT * FROM USER WHERE USERNAME = '$username';
        """.trimIndent(), null)

        with (cursor) {
            while (moveToNext()) {
                users += arrayOf(
                    User(
                        getString(getColumnIndexOrThrow(("USERNAME"))),
                        getString(getColumnIndexOrThrow("EMAIL")),
                        getString(getColumnIndexOrThrow("SENHA")),
                    )
                )
            }
        }

        db.close()

        if(users.isEmpty()) return null
        return users.first()
    }

    fun addGame(game: Game): Game {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put("NAME", game.name)
            put("STATUS", game.status)
            put("RATE", game.rate)
            put("USER", game.user)
        }

        db.insertOrThrow("GAME", null, values)
        db.close()

        return game
    }

    fun getGames(username: String, status: String): List<Game> {
        val db = this.readableDatabase
        var games : Array<Game> = emptyArray()
        val cursor = db.rawQuery("""
            SELECT * FROM GAME WHERE USER = '$username' AND STATUS = '$status'
        """.trimIndent(), null)

        with (cursor) {
            while (moveToNext()) {
                games += arrayOf(
                    Game(
                        getInt(getColumnIndexOrThrow("ID")),
                        getString(getColumnIndexOrThrow("NAME")),
                        getString(getColumnIndexOrThrow("STATUS")),
                        getString(getColumnIndexOrThrow("RATE")),
                        getString(getColumnIndexOrThrow("USER")),
                    )
                )
            }
        }

        db.close()

        return games.toList()
    }

    fun removeGame(game: Game) {
        try {
            val db = this.writableDatabase

            db.delete("GAME", "ID=?", arrayOf( game.id.toString() ))
            db.close()
        } catch (e: Exception) {
            e.message?.let { Log.e("ERRO", it) }
        }

    }
}
