package com.example.listagaymer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.listagaymer.database.DataBaseGaymerList
import com.example.listagaymer.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db = DataBaseGaymerList(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        openOrCreateDatabase("GAYMERLIST", MODE_PRIVATE, null)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!isLogged()) logout()

        val navController = findNavController(R.id.fragment_main)
        val navBottom: BottomNavigationView = binding.bottomViewMain
        val fab: FloatingActionButton = binding.fab
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
            navController.navigate(R.id.addGameFragment)
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
