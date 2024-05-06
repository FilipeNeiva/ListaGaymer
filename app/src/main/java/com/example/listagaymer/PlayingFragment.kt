package com.example.listagaymer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listagaymer.adapter.GameListAdapter
import com.example.listagaymer.data.Game
import com.example.listagaymer.database.DataBaseGaymerList
import com.example.listagaymer.databinding.FragmentPlayingBinding

class PlayingFragment : Fragment() {

    private lateinit var binding: FragmentPlayingBinding
    private lateinit var adapter: GameListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            playingRecycler.setHasFixedSize(true)
            playingRecycler.layoutManager = LinearLayoutManager(activity)
        }

    }

    override fun onResume() {
        super.onResume()
        val db = DataBaseGaymerList(requireContext().applicationContext)

        val user = getUserData(requireContext())
        var games: List<Game> = emptyList()
        if (user != null){ games = db.getGames(user.username, "Jogando") }

        adapter = GameListAdapter(requireActivity(), games)
        binding.playingRecycler.adapter = adapter
    }
}