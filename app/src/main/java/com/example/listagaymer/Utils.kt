package com.example.listagaymer

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listagaymer.data.User
import com.example.listagaymer.database.DataBaseGaymerList
import com.example.listagaymer.ui.activity.GameListActivity

fun login(username: String, password: String, context: Context): Boolean {
    val db = DataBaseGaymerList(context)

    val user: User? = db.getUser(username)
    if (user == null) {
        Toast.makeText(context, "Usuário não encontrado", Toast.LENGTH_LONG).show()
        return false
    } else if (user.senha != password) {
        Toast.makeText(context, "Senha incorreta", Toast.LENGTH_LONG).show()
        return false
    } else {
        //Gravar usuario
        val sharedPreferences = context.getSharedPreferences("GAYMERLIST", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER", user.username)
        editor.apply()

        //mandar pra main activity
        val intent = Intent(context, GameListActivity::class.java)
        context.startActivity(intent)

        return true
    }
}

fun getUserData(context: Context): User? {
    val db = DataBaseGaymerList(context)

    val sharedPreferences = context.getSharedPreferences("GAYMERLIST", AppCompatActivity.MODE_PRIVATE);
    val username = sharedPreferences.getString("USER", "")

    if (username != null) {
        return db.getUser(username)
    }
    return null
}