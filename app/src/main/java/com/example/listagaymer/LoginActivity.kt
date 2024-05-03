package com.example.listagaymer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        binding.loginButton.setOnClickListener{
            if (login(username.text.toString(), password.text.toString(), this)) {
                finish()
            }
        }

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
}
