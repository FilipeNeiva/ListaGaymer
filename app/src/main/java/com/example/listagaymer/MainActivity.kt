package com.example.listagaymer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.listagaymer.database.DataBaseGaymerList
import com.example.listagaymer.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        openOrCreateDatabase("GAYMERLIST", MODE_PRIVATE, null)
        DataBaseGaymerList(applicationContext)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!isLogged()) logout()

        val fab: FloatingActionButton = binding.fab
        val navController = findNavController(R.id.fragment_main)
        val navBottom: BottomNavigationView = binding.bottomViewMain
        navBottom.setupWithNavController(navController)

        navBottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.playingFragment -> navController.navigate(R.id.playingFragment)
                R.id.doneGameFragment -> navController.navigate(R.id.doneGameFragment)
                R.id.intentGameFragment -> navController.navigate(R.id.intentGameFragment)
            }
            true
        }

        fab.setOnClickListener{
            val intent = Intent(this, AddGameActivity::class.java)
            startActivity(intent)
        }

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
