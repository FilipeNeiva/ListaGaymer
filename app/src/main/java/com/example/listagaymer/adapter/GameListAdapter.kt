package com.example.listagaymer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listagaymer.data.Game
import com.example.listagaymer.database.DataBaseGaymerList
import com.example.listagaymer.databinding.GameListAdapterBinding

class GameListAdapter(var context: Context, private var games: List<Game>) :
    RecyclerView.Adapter<GameListAdapter.ItemViewHolder>() {
    private var listGames: MutableList<Game> =
        if (games.isNotEmpty()) games as MutableList<Game> else mutableListOf<Game>()

    inner class ItemViewHolder(private val binding: GameListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val gameNameElement: TextView
        val removeGameBtn: ImageButton

        @SuppressLint("SetTextI18n")
        fun bind(game: Game?) {
            if (game != null) {
                gameNameElement.text = game.name
            }
            removeGameBtn.setOnClickListener {
                if (game != null) {
                    deleteGame(game)
                }
            }
        }

        init {
            gameNameElement = binding.gameName
            removeGameBtn = binding.removeGameBtn
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GameListAdapter.ItemViewHolder {
        val binding =
            GameListAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameListAdapter.ItemViewHolder, position: Int) {
        holder.bind(listGames[position])
    }

    override fun getItemCount(): Int {
        return listGames.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteGame(game: Game) {

        val db = DataBaseGaymerList(context)
        db.removeGame(game)
        db.close()

        try {
            listGames.remove(game)
            notifyDataSetChanged()
        } catch (e: Exception) {
            listGames = mutableListOf<Game>()
            notifyDataSetChanged()
        }
    }
}

