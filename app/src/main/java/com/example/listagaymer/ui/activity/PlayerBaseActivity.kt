package com.example.listagaymer.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.listagaymer.database.AppDatabase
import com.example.listagaymer.extentions.goTo
import com.example.listagaymer.model.Player
import com.example.listagaymer.preferences.LoggedPlayerPreferences
import com.example.listagaymer.preferences.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class PlayerBaseActivity : AppCompatActivity() {

    private val playerDao by lazy {
        AppDatabase.instance(this).playerDao()
    }

    private var _player: MutableStateFlow<Player?> = MutableStateFlow(null)
    val player: StateFlow<Player?> = _player


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            checkPlayerLogged()
        }
    }

    private suspend fun checkPlayerLogged() {
        dataStore.data.collect { preferences ->
            preferences[LoggedPlayerPreferences]?.let { playerUsername ->
                getPlayer(playerUsername)
            } ?: vaiParaLogin()
        }
    }

    private suspend fun getPlayer(playerUsername: String): Player? {
        return playerDao.getPlayerById(playerUsername).firstOrNull().also {
                _player.value = it
            }
    }

    protected suspend fun logoutPlayer() {
        dataStore.edit { preferences ->
            preferences.remove(LoggedPlayerPreferences)
        }
    }

    private fun vaiParaLogin() {
        goTo(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }
}