package com.example.listagaymer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.listagaymer.databinding.FragmentPlayingBinding

class PlayingFragment : Fragment() {

    private lateinit var binding: FragmentPlayingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}