package com.onopry.whac_a_mole.view.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.onopry.whac_a_mole.R
import com.onopry.whac_a_mole.databinding.FragmentStartScreenBinding

class StartScreenFragment : Fragment() {

    private lateinit var binding: FragmentStartScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartScreenBinding.inflate(inflater, container, false)

        binding.startGameBtn.setOnClickListener { startGame() }

        return binding.root
    }

    private fun startGame() {
        findNavController().navigate(R.id.action_startScreenFragment_to_gameFragment)
    }

}