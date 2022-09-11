package com.onopry.whac_a_mole.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onopry.whac_a_mole.*
import com.onopry.whac_a_mole.model.Mole
import java.util.*
import kotlin.math.floor
import kotlin.random.Random

private const val TAG = "GameViewModelTAG"

class GameViewModel: ViewModel() {
    private val _gameField = MutableLiveData<Array<Array<Boolean>>>()
    val gameField: LiveData<Array<Array<Boolean>>> = _gameField

    private val _gameScore = MutableLiveData<Int>()
    val gameScore: LiveData<Int> = _gameScore

    private val _timer = MutableLiveData<Long>()
    val timer: LiveData<Long> = _timer

    private val _isGameFinished = MutableLiveData<Boolean>()
    val isGameFinished: LiveData<Boolean> = _isGameFinished

    // Fake queue for current mole's on the field
    private val moleQueue = LinkedList<Mole>()

    fun initGame() {
        clearGameField()
        _gameScore.value = 0
        startGameTimer().start()
    }

    private fun startGameTimer(): CountDownTimer {
        _timer.value = 0
        return object : CountDownTimer(GAME_TIME, GAME_TICK){
            var secs = (GAME_TIME/1000).toInt()
            override fun onTick(time: Long) {
                Log.d(TAG, "onTick: long:${time} rounded:${floor(time.toFloat()/1000)}")

                _timer.value = time
                if (time < secs*1000) {
                    generateMole(time)
                    secs -= 1
                }

                if (moleQueue.isNotEmpty()) {
                    if (time < moleQueue.first().endLifeTime) {
                        removeMole(moleQueue.first)
                    }
                }

                Log.d(TAG, "MoleQueue size = ${moleQueue.size} ")

            }

            override fun onFinish() {
                finishGame()
            }
        }
    }

    private fun finishGame(){
        _isGameFinished.postValue(true)
        _timer.value = 0
//        _gameScore.value = 0
        clearGameField()
    }

    private fun clearGameField(){
        _gameField.postValue(Array(ROWS) { Array(COLUMNS) {false} })
    }

    // creating new mole on random cell and refresh game field
    fun generateMole(startLifeTimeMole: Long) {
        if (Random.nextFloat() >= MOLE_SPAWN_RATE) {
            val molePosColumn = Random.nextInt(0, COLUMNS)
            val molePosRow = Random.nextInt(0, ROWS)
            if (_gameField.value?.get(molePosColumn)?.get(molePosRow) != true) {
                val newField = _gameField.value?.copyOf()
                newField?.get(molePosColumn)?.set(molePosRow, true)
                moleQueue.offerFirst(
                    Mole(molePosColumn, molePosRow, startLifeTimeMole)
                )
                _gameField.postValue(newField!!)
            }
        }
    }

    // removing mole from game field and mole queue, using for self-destruction
    fun removeMole(mole: Mole) {
        if (mole.col > COLUMNS || mole.row > ROWS) throw Exception("Cant kill mole: Invalid indices")
        val newField = _gameField.value?.copyOf()
        newField?.get(mole.col)?.set(mole.row, false)
        _gameField.postValue(newField!!)
        moleQueue.removeFirst()
    }

    fun onUserClick(row: Int, column: Int) {
        if (_gameField.value?.get(column)?.get(row) == true) {
            killMole(row, column)
        }
    }

    // when user tapped mole this method delete it's from queue and game field
    private fun killMole(row: Int, column: Int) {
        val field = _gameField.value?.copyOf()

        val moleToKill = moleQueue.find { mole -> mole.col == column && mole.row == row }
        if (moleToKill != null) moleQueue.remove(moleToKill)
        field?.get(column)?.set(row, false)
        _gameField.postValue(field!!)
        _gameScore.value = _gameScore.value?.plus(1)

    }
}