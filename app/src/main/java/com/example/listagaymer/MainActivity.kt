package com.example.listagaymer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.listagaymer.database.DataBaseGaymerList
import com.example.listagaymer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db = DataBaseGaymerList(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        openOrCreateDatabase("GAYMERLIST", MODE_PRIVATE, null)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!isLogged()) logout()

//        binding.logoutButton.setOnClickListener{ logout() }
    }

    private fun isLogged(): Boolean {
        val sharedPreferences = getSharedPreferences("GAYMERLIST", MODE_PRIVATE);
        val username = sharedPreferences.getString("USER", "")
        return username != ""
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("GAYMERLIST", MODE_PRIVATE);
        val editor = sharedPreferences.edit()
        editor.remove("USER")
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        finish()
    }
}
