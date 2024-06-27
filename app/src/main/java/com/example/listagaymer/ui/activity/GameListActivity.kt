package com.example.listagaymer.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.listagaymer.R
import com.example.listagaymer.database.AppDatabase
import com.example.listagaymer.databinding.ActivityGameListBinding
import com.example.listagaymer.extentions.toast
import com.example.listagaymer.model.Game
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class GameListActivity : PlayerBaseActivity() {
    private val binding by lazy {
        ActivityGameListBinding.inflate(layoutInflater)
    }
    private val gameDao by lazy {
        AppDatabase.instance(this).gameDao()
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    //    private var actionMode: ActionMode? = null
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        makeNavBarNavigation()
        makeAppBarConfiguration()
        setupActionBarWithNavController(navController, appBarConfiguration)

        dialogAddGameFactory()
        makeAddGameButton()
        logoutButtonConfiguration()
        namePlayerDrawerSetup()
    }

    private fun namePlayerDrawerSetup() {
        lifecycleScope.launch {
            player.collect { player ->
                player?.let {
                    binding.activateNavMenu.playerName.text = it.username
                }
            }
        }
    }

    private fun logoutButtonConfiguration() {
        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                logoutPlayer()
            }
        }
    }

    private fun makeAppBarConfiguration() {
        val drawerLayout: DrawerLayout = binding.main

        setSupportActionBar(binding.activateToolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.playingFragment,
                R.id.doneGameFragment,
                R.id.intentGameFragment
            ), drawerLayout
        )
    }

    private fun makeNavBarNavigation() {
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
    }

    private fun makeAddGameButton() {
        binding.fab.setOnClickListener {
            dialog.show()
        }
//        .setOnClickListener {
//            if (actionMode == null) {
//                actionMode = startSupportActionMode(callback)
//                supportActionBar?.hide()
//            }
//        }
    }

    private fun dialogAddGameFactory() {
        dialog = Dialog(this)
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
            if (navController.currentDestination?.label == "Jogando") 0
            else if (navController.currentDestination?.label == "Finalizados") 1 else 2
        dropdown.setSelection(item)

        dialog.findViewById<Button>(R.id.add_game_btn).setOnClickListener {
            if (isGameValid(nameGame.text.toString(), dropdown.selectedItem.toString())) {
                addGame(nameGame.text.toString(), dropdown.selectedItem.toString())
            }
        }

        dialog.findViewById<AppCompatButton>(R.id.btn_cancel_new_game).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun isGameValid(name: String, status: String): Boolean {
        if (name == "") {
            toast("o nome não pode ser vazio")
            return false
        }

        if (name.length < 2) {
            toast("o nome deve ter mais de 2 caracteres")
            return false
        }

        return true
    }

    private fun addGame(name: String, status: String) {
        player.value?.let { player ->

            val game = Game(
                name = name,
                status = status,
                playerId = player.username,
                rate = null
            )

            lifecycleScope.launch {
                gameDao.create(game)
                dialog.dismiss()
                navController.currentDestination?.id?.let { navController.navigate(it) }
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    // tetando fazer action mode

//    private val callback: ActionMode.Callback = object : ActionMode.Callback {
//        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
//            if (mode != null) {
//                mode.menuInflater.inflate(R.menu.toolbar_menu, menu)
//                mode.title = "Ações"
//            }
//            return true
//        }
//
//        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
//            return false
//        }
//
//        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem): Boolean {
//            when (item.itemId) {
//                R.id.check_item -> {
//
//                }
//
//                R.id.delete_item -> {
//
//                }
//            }
//            return false
//        }
//
//        override fun onDestroyActionMode(mode: ActionMode?) {
//            actionMode = null
//            supportActionBar?.show()
//        }
//
//    }
}
