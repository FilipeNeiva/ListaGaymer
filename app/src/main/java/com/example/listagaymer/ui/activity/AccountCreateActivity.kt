package com.example.listagaymer.ui.activity

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.listagaymer.database.AppDatabase
import com.example.listagaymer.databinding.ActivityAccountCreateBinding
import com.example.listagaymer.extentions.goTo
import com.example.listagaymer.extentions.toast
import com.example.listagaymer.model.Player
import com.example.listagaymer.preferences.LoggedPlayerPreferences
import com.example.listagaymer.preferences.dataStore
import kotlinx.coroutines.launch


class AccountCreateActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAccountCreateBinding.inflate(layoutInflater)
    }

    private val playerDao by lazy {
        AppDatabase.instance(this).playerDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        makeRegisterButton()
    }

    private fun makeRegisterButton() {
        binding.registerButton.setOnClickListener {
            createAcctount()
        }
    }

    fun createAcctount() {
        lifecycleScope.launch {
            try {
                val player = getPlayer()
                playerDao.create(player)
                playerDao.authenticate(player.username, player.password)?.let { player ->
                    dataStore.edit { preferences ->
                        preferences[LoggedPlayerPreferences] = player.username
                    }
                    goTo(GameListActivity::class.java) {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                } ?: toast("Falha na autentificação")
            } catch (e: SQLiteConstraintException) {
                Log.e("ERRO", e.stackTraceToString())
                toast("Esse usuário já existe!")
            } catch (e: Exception) {
                e.message?.let { Log.e("ERRO", it) }
                e.message?.let { toast(it) }
            }
        }
    }

    private fun getPlayer(): Player {
        val username = binding.userTextInput.text.toString()
        val email = binding.emailTextInput.text.toString()
        val password = binding.passwordTextInput.text.toString()
        val repeatPassword = binding.repeatPasswordTextInput.text.toString()

        isValid(username, email, password, repeatPassword)
        return Player(
            username = username,
            email = email,
            password = password
        )

    }

    fun isValid(
        username: String,
        email: String,
        password: String,
        repeatPassword: String
    ) {
        if (username.length < 4) throw Exception("O nome de usuário deve ter pelo menos 4 caracteres!")
        if (!isValidEmail(email)) throw Exception("Entre com um endereço de e-mail valido!")
        if (password.length < 4) throw Exception("A senha deve ter pelo menos 4 caracteres!")
        if (password != repeatPassword) throw Exception("As senhas não coincidem!")
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return target?.let { email ->
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        } ?: false
    }
}
