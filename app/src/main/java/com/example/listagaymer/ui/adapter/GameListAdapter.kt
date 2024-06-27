package com.example.listagaymer.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.listagaymer.R
import com.example.listagaymer.data.Game
import com.example.listagaymer.databinding.GameListAdapterBinding

class GameListAdapter(var context: Context, games: List<Game>) :
    RecyclerView.Adapter<GameListAdapter.ItemViewHolder>() {
    private var listGames: MutableList<Game> =
        if (games.isNotEmpty()) games as MutableList<Game> else mutableListOf()
    private var listMarkGames: MutableList<Game> = mutableListOf()

    inner class ItemViewHolder(binding: GameListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val gameNameElement: TextView = binding.gameName
        private val removeGameBtn: ImageButton = binding.removeGameBtn
        private val cardItem: CardView = binding.cardItem

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

            cardItem.setOnLongClickListener {
                if (game != null && listMarkGames.isEmpty()) {
                    toggleGame(game)
                }
                true
            }

            cardItem.setOnClickListener {
                game?.let { item ->
                    if (listMarkGames.isNotEmpty())
                        toggleGame(item)
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun toggleGame(game: Game) {
            if (listMarkGames.contains(game)) listMarkGames.remove(game)
            else listMarkGames.add(game)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            GameListAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val game = listGames[position]
        holder.bind(game)
        if (listMarkGames.contains(game)) holder.itemView.setBackgroundResource(R.drawable.item_selected)
        else holder.itemView.setBackgroundResource(R.drawable.item_card)
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
            listGames = mutableListOf()
            notifyDataSetChanged()
        }
    }
}

