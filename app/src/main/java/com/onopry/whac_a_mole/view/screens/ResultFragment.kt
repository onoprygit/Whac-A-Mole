package com.onopry.whac_a_mole.view.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.onopry.whac_a_mole.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding

    private var userScore: Int? = null
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        userScore = args.userScore

        // todo: change text hardcode to
        binding.resultRecordTitleTv.text = "Best score: ${readScore(args.userScore)}"
        binding.resultScoreTitleTv.text = "Your score: ${args.userScore}"
        binding.restartGameBnt.setOnClickListener { restartGame() }
        binding.menuGameBnt.setOnClickListener { openMenu() }

        return binding.root
    }

    private fun openMenu() {}
    private fun restartGame() {}
    private fun readScore(lastGameScore: Int) {}
}