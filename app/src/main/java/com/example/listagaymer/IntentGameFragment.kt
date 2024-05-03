package com.example.listagaymer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.listagaymer.databinding.FragmentIntentGameBinding

class IntentGameFragment : Fragment() {

    private lateinit var binding: FragmentIntentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntentGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}