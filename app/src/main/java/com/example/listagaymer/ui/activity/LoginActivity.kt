package com.example.listagaymer.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.listagaymer.database.AppDatabase
import com.example.listagaymer.databinding.ActivityLoginBinding
import com.example.listagaymer.extentions.goTo
import com.example.listagaymer.preferences.LoggedPlayerPreferences
import com.example.listagaymer.preferences.dataStore
import com.example.listagaymer.extentions.toast
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val playerDao by lazy {
        AppDatabase.instance(this).playerDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        makeAccountCreateButton()
        makeLoginButton()
    }

    private fun makeLoginButton() {
        val username = binding.userTextInput.text.toString()
        val password = binding.passwordTextInput.text.toString()

        binding.loginButton.setOnClickListener {
            lifecycleScope.launch {
                playerDao.authenticate(username, password)?.let { player ->
                    dataStore.edit { preferences ->
                        preferences[LoggedPlayerPreferences] = player.username
                    }
                    goTo(GameListActivity::class.java) {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                } ?: toast("Falha na autentificação")

            }
        }
    }

    private fun makeAccountCreateButton() {
        binding.newAccountText.setOnClickListener {
            binding.newAccountText.isEnabled = false
            goTo(AccountCreateActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.newAccountText.isEnabled = true
    }
}
