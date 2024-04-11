package com.example.listagaymer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listagaymer.data.User
import com.example.listagaymer.database.DataBaseGaymerList
import com.example.listagaymer.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val db = DataBaseGaymerList(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.userTextInput
        val password = binding.passwordTextInput

        binding.loginButton.setOnClickListener{ login(username.text.toString(), password.text.toString()) }

        binding.newAccountText.setOnClickListener{
            binding.newAccountText.isEnabled = false
            val myIntent = Intent(
                this,
                AccountCreateActivity::class.java
            )

            this.startActivity(myIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.newAccountText.isEnabled = true
    }

    fun login(username: String, password: String) {
        binding.newAccountText.isEnabled = false

        val user: User? = db.getUser(username)
        if (user == null) {
            binding.newAccountText.isEnabled = true
            Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_LONG).show()
        } else if (user.senha != password) {
            binding.newAccountText.isEnabled = true
            Toast.makeText(this, "Senha incorreta", Toast.LENGTH_LONG).show()
        } else {
            //Gravar usuario
            val sharedPreferences = getSharedPreferences("GAYMERLIST", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("USER", user.username)
            editor.apply()

            //mandar pra main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}
