package com.example.listagaymer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.listagaymer.database.AppDatabase
import com.example.listagaymer.databinding.FragmentPlayingBinding
import com.example.listagaymer.ui.activity.PlayerBaseActivity
import com.example.listagaymer.ui.adapter.GameListAdapter
import kotlinx.coroutines.launch

class PlayingFragment : Fragment() {

    private lateinit var binding: FragmentPlayingBinding
    private lateinit var adapter: GameListAdapter
    private val gameDao by lazy {
        context?.let { AppDatabase.instance(it).gameDao() }
    }

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
            playingRecycler.layoutManager = GridLayoutManager(activity, 2)
        }

        loadGames()
    }

    private fun loadGames() {
        (activity as? PlayerBaseActivity)?.let { activity ->
            lifecycleScope.launch {
                activity.player.collect { player ->
                    player?.let { playerLoad ->
                        gameDao
                            ?.getGames(playerLoad.username, "Jogando")
                            ?.collect { games ->
                                adapter = GameListAdapter(requireActivity(), games)
                                binding.playingRecycler.adapter = adapter
                            }
                    }
                }
            }

        }
    }

}