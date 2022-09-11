package com.onopry.whac_a_mole.view.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.onopry.whac_a_mole.R
import com.onopry.whac_a_mole.databinding.FragmentResultBinding

private const val APP_PREF = "best_score"
private const val USER_BEST_SCORE = "userBestScore"

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

    private fun openMenu() {
        findNavController().navigate(R.id.action_resultFragment_to_startScreenFragment)
    }

    private fun restartGame() {
        findNavController().navigate(R.id.action_resultFragment_to_gameFragment)
    }

    private fun writeScore(score: Int) {
        requireActivity()
            .getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
            .edit()
            .putInt(USER_BEST_SCORE, score)
            .apply()
    }

    private fun readScore(lastGameScore: Int): Int {
        val bestScorePrefs = requireActivity().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)

        if (!bestScorePrefs.contains(USER_BEST_SCORE)) {
            writeScore(lastGameScore)
            return lastGameScore
        }

        val bestScore = bestScorePrefs.getInt(USER_BEST_SCORE, lastGameScore)
        return if (bestScore < lastGameScore)
            lastGameScore
                .also { writeScore(it) }
        else bestScore
    }
}