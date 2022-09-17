package com.onopry.whac_a_mole.viewmodels

import android.os.CountDownTimer
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

    private val gameTimer = whacAMoleGame.initGameTimer()

/*    private fun initGameTimer(): CountDownTimer {
        return object : CountDownTimer(GAME_TIME, GAME_TICK) {
            var secs = (GAME_TIME / 1000).toInt()
            override fun onTick(time: Long) {
                this@WhacAMoleGame._time.value = time
                if (time < secs * 1000) {
                    generateMole(time)
                    secs -= 1
                }

                if (moleQueue.isNotEmpty()) {
                    if (time < moleQueue.first().endLifeTime)
                        removeMole(moleQueue.first)
                }
            }

            override fun onFinish() {
                whacAMoleGame.finishGame()
            }
        }
    }*/

    fun startGame() {
        whacAMoleGame.initGame()
        gameTimer.start()
    }

    // todo: rename to correct signature
    fun onUserClick(row: Int, column: Int) {
        whacAMoleGame.trackUserClick(row, column)
    }

    override fun onCleared() {
        super.onCleared()
//        whacAMoleGame.stopTimer()
        gameTimer.cancel()
    }
}