package com.example.listagaymer

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.listagaymer.data.Game
import com.example.listagaymer.database.DataBaseGaymerList
import com.example.listagaymer.databinding.ActivityAddGameBinding

class AddGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddGameBinding
    private val db = DataBaseGaymerList(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dropdown: Spinner = binding.gameStatusSpinner
        val items = arrayOf("Jogando", "Finalizado", "Pretendo jogar")
        val adapter = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, items)
        val add = binding.addGameBtn
        val nameElement = binding.gameName
        val statusElement = binding.gameStatusSpinner

        dropdown.setAdapter(adapter);

        add.setOnClickListener{ addGame(nameElement.text.toString(), statusElement.selectedItem.toString()) }
    }

    fun addGame(name: String, status: String) {
        val user = getUserData(this)
        if (user != null) {
            val game = Game(name = name, status = status, user = user.username, id = null, rate = null)
            try {
                db.addGame(game)
                finish()
            } catch (e: Exception) {
                e.message?.let { Log.e("ERRO", it) }
            }
        }
    }
}