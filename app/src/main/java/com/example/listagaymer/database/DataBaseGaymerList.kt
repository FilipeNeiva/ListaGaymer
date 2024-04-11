package com.example.listagaymer.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.listagaymer.data.User

class DataBaseGaymerList(context: Context): SQLiteOpenHelper(context, DataBaseGaymerList.DB_NAME, null, DataBaseGaymerList.DB_VERSION) {
    companion object {
        private const val DB_VERSION = 1
        private val DB_NAME = "GAYMERLIST"
    }

    override fun onCreate(db: SQLiteDatabase?) = onUpgrade(db, 0, DB_VERSION)

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 0) {
            db?.execSQL("CREATE TABLE IF NOT EXISTS USER (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, EMAIL TEXT, SENHA TEXT)")
        }
    }

    fun addUser(user: User): User? {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put("USERNAME", user.username)
            put("EMAIL", user.email)
            put("SENHA", user.senha)
        }

        val retorno = try {
            db.insert("USER", null, values)
        } catch (e: Exception) {
            0
        } finally {
            db.close()
        }

        return if (retorno == 0L) { null } else { user }

    }

    fun getUser(username: Int): User {
        val db = this.readableDatabase
        var users : Array<User> = emptyArray()


        val cursor = db.rawQuery("""
            SELECT * FROM USER WHERE ID = $username
        """.trimIndent(), null)

        with (cursor) {
            while (moveToNext()) {
                users += arrayOf(
                    User(
                        getInt(getColumnIndexOrThrow("ID")),
                        getString(getColumnIndexOrThrow(("USERNAME"))),
                        getString(getColumnIndexOrThrow("EMAIL")),
                        getString(getColumnIndexOrThrow("SENHA")),
                    )
                )
            }
        }

        db.close()

        return users.first()
    }
}
