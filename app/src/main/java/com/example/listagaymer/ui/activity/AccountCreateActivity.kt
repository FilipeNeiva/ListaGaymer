package com.example.listagaymer.ui.activity

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listagaymer.data.User
import com.example.listagaymer.database.DataBaseGaymerList
import com.example.listagaymer.databinding.ActivityAccountCreateBinding
import com.example.listagaymer.login


class AccountCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountCreateBinding
    private val db = DataBaseGaymerList(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val username = binding.userTextInput
        val email = binding.emailTextInput
        val password = binding.passwordTextInput
        val repeatPassword = binding.repeatPasswordTextInput
        val register = binding.registerButton

        register.setOnClickListener{
            createAcctount(
                username.text.toString(),
                email.text.toString(),
                password.text.toString(),
                repeatPassword.text.toString()
            )
        }

    }

    fun createAcctount(username: String, email: String, password: String, repeatPassword: String) {
        try {
            if (isValid(username, email, password, repeatPassword)) {
                db.addUser(User(
                    username,
                    email,
                    password
                ))
                if (login(username, password, this)) {
                    finish()
                }
            }
        } catch (e: SQLiteConstraintException) {
            Log.e("ERRO", e.stackTraceToString())
            Toast.makeText(this, "Esse usuário já existe!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.message?.let { Log.e("ERRO", it) }
            e.message?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    fun isValid(username: String, email: String, password: String, repeatPassword: String): Boolean {
        if (username.length < 4) throw Exception("O nome de usuário deve ter pelo menos 4 caracteres!")
        if (!isValidEmail(email)) throw Exception("Entre com um endereço de e-mail valido!")
        if (password.length < 4) throw Exception("A senha deve ter pelo menos 4 caracteres!")
        if (password != repeatPassword) throw Exception("As senhas não coincidem!")
        return true
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}
