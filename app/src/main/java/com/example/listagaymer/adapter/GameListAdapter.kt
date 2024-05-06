package com.example.listagaymer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listagaymer.data.Game
import com.example.listagaymer.databinding.GameListAdapterBinding

class GameListAdapter(var context: Context, var games: List<Game>) :
    RecyclerView.Adapter<GameListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: GameListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val gameNameElement: TextView

        fun bind(game: Game?) {
            binding.gameName.text = game?.name
        }

        init {
            gameNameElement = binding.gameName
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
        holder.bind(games[position])

//        holder.itemView.setOnClickListener {
//            onItemClick?.invoke(orderList[position])
//        }
    }

    override fun getItemCount(): Int {
        return games.size
    }
}

