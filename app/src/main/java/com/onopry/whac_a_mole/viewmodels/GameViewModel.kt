package com.onopry.whac_a_mole.viewmodels

import androidx.lifecycle.ViewModel
import com.onopry.whac_a_mole.*
import com.onopry.whac_a_mole.model.Mole
import com.onopry.whac_a_mole.model.WhacAMoleGame
import java.util.*
import kotlin.random.Random

private const val TAG = "GameViewModelTAG"

class GameViewModel: ViewModel() {
    private val whacAMoleGame = WhacAMoleGame(ROWS, COLUMNS)

    val gameField = whacAMoleGame.gameField
    val score = whacAMoleGame.gameScore
    val time = whacAMoleGame.time
    val isGameFinished = whacAMoleGame.isGameFinished

    fun startGame() { whacAMoleGame.initGame() }

    // todo: rename to correct signature
    fun onUserClick(row: Int, column: Int) {
        whacAMoleGame.trackUserClick(row, column)
    }

    override fun onCleared() {
        super.onCleared()
        whacAMoleGame.stopTimer()
    }
}