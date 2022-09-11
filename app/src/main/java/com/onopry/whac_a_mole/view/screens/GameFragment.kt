package com.onopry.whac_a_mole.view.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.onopry.whac_a_mole.databinding.FragmentGameBinding
import com.onopry.whac_a_mole.viewmodels.GameViewModel

private const val TAG = "GameFragment_TAG"

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        // Starting game
        viewModel.initGame()

        // Observing state of game field
        viewModel.gameField.observe(viewLifecycleOwner) { field ->
            binding.gameFieldView.gameField = field
            Log.d(TAG, "onCreateView: ${field.toString()}")
        }
        // Updating timer for user
        viewModel.timer.observe(viewLifecycleOwner) { time ->
            binding.gameTimerValueTv.text = "${time/1000}"
        }
        // Checking is game finished
        viewModel.isGameFinished.observe(viewLifecycleOwner) { isGameFinished ->
            if (isGameFinished)
                viewModel.gameScore.value?.let { openResults(it) }
            Log.d(TAG, "isGameFinished: $isGameFinished")
        }
        // Updating score
        viewModel.gameScore.observe(viewLifecycleOwner) { score ->
//            this.score = score
            binding.scoreValueTv.text = "$score"
        }

        // Observing for user's touches
        binding.gameFieldView.actionListener = { row, column ->
            viewModel.onUserClick(row, column)
            Log.d(TAG, "onCreateView: $row : $column}")
        }

        return binding.root
    }

    private fun openResults(score: Int) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToResultFragment(score)
        )
    }

}