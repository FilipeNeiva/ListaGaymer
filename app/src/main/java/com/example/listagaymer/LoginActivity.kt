package com.example.listagaymer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.listagaymer.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        openOrCreateDatabase("GAYMERLIST", MODE_PRIVATE, null)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
