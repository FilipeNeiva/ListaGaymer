package com.example.listagaymer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.listagaymer.databinding.FragmentDoneGameBinding
import com.example.listagaymer.ui.adapter.GameListAdapter

class DoneGameFragment : Fragment() {

    private lateinit var binding: FragmentDoneGameBinding
    private lateinit var adapter: GameListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoneGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            doneGameRecycler.setHasFixedSize(true)
            doneGameRecycler.layoutManager = GridLayoutManager(activity, 2)
        }

    }

//    override fun onResume() {
//        super.onResume()
//        val db = DataBaseGaymerList(requireContext().applicationContext)
//
//        val user = getUserData(requireContext())
//        var games: List<Game> = emptyList()
//        if (user != null) {
//            games = db.getGames(user.username, "Finalizado")
//        }
//
//        adapter = GameListAdapter(requireActivity(), games)
//        binding.doneGameRecycler.adapter = adapter
//    }
}