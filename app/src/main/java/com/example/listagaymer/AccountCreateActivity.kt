package com.example.listagaymer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.listagaymer.databinding.ActivityAccountCreateBinding
import com.example.listagaymer.databinding.ActivityLoginBinding

class AccountCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
