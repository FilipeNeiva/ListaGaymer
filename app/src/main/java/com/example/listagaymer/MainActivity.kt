package com.example.listagaymer

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.listagaymer.data.Game
import com.example.listagaymer.database.DataBaseGaymerList
import com.example.listagaymer.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        openOrCreateDatabase("GAYMERLIST", MODE_PRIVATE, null)
        DataBaseGaymerList(applicationContext)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isLogged()) logout()

        val user = getUserData(this)
        val fab: FloatingActionButton = binding.fab
        val drawerLayout: DrawerLayout = binding.main
        navController = findNavController(R.id.fragment_main)
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

        fab.setOnClickListener { dialogFacture(this) }

        setSupportActionBar(binding.activateToolbar)


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.playingFragment,
                R.id.doneGameFragment,
                R.id.intentGameFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.btnLogout.setOnClickListener { logout() }
        binding.activateNavMenu.playerName.text = user?.username
    }

    private fun dialogFacture(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.modal_add_game)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val items = arrayOf("Jogando", "Finalizado", "Pretendo jogar")
        val adapter = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, items)
        val dropdown = dialog.findViewById<Spinner>(R.id.game_status_spinner)
        dropdown.setAdapter(adapter)

        val nameGame = dialog.findViewById<EditText>(R.id.game_name)
        val item =
            if (navController.currentDestination?.label == "Jogando") 0 else if (navController.currentDestination?.label == "Finalizados") 1 else 2
        dropdown.setSelection(item)

        dialog.findViewById<Button>(R.id.add_game_btn).setOnClickListener {
            if (isGameValid(nameGame.text.toString(), dropdown.selectedItem.toString())) {
                addGame(nameGame.text.toString(), dropdown.selectedItem.toString(), dialog)
            }
        }

        dialog.findViewById<AppCompatButton>(R.id.btn_cancel_new_game).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun isGameValid(name: String, status: String): Boolean {
        if (name == "") {
            Toast.makeText(this, "o nome não pode ser vazio", Toast.LENGTH_SHORT).show()
            return false
        }

        if (name.length < 2) {
            Toast.makeText(this, "o nome deve ter mais de 2 caracteres", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun addGame(name: String, status: String, dialog: Dialog) {
        val user = getUserData(this)
        val db = DataBaseGaymerList(this)

        if (user != null) {
            val game =
                Game(name = name, status = status, user = user.username, id = null, rate = null)
            try {
                db.addGame(game)
                dialog.dismiss()
                navController.currentDestination?.id?.let { navController.navigate(it) }
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                e.message?.let { Log.e("ERRO", it) }
            }
        }
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
