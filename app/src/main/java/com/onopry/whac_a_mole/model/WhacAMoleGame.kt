package com.onopry.whac_a_mole.model

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onopry.whac_a_mole.*
import java.util.*
import kotlin.random.Random

const val TAG = "WhacAMoleGameDebugTag"

enum class Cell {
    EMPTY,
    HAS_MOLE
}

class WhacAMoleGame (
    private val rows: Int,
    private val columns: Int
    ) {

    private val _gameField = MutableLiveData<Array<Array<Cell>>>()
    val gameField: LiveData<Array<Array<Cell>>> = _gameField

    private val _gameScore = MutableLiveData<Int>()
    val gameScore: LiveData<Int> = _gameScore

    private val _time = MutableLiveData<Long>()
    val time: LiveData<Long> = _time

    private val _isGameFinished = MutableLiveData<Boolean>()
    val isGameFinished: LiveData<Boolean> = _isGameFinished

    // Fake queue for current mole's on the field
    private val moleQueue = LinkedList<Mole>()

    private val gameTimer = initGameTimer()

    fun getCell(row: Int, column: Int): Cell {
//        if (row < 0 || column < 0 || row >= rows || column >= columns) return Cell.EMPTY
        return _gameField.value?.get(column)?.get(row) ?: Cell.EMPTY
    }

    fun setCell(row: Int, column: Int, cell: Cell) {
        if (row < 0 || column < 0 || row >= rows || column >= columns) return
        _gameField.value?.get(row)?.set(column, cell)
    }

    fun initGame() {
        clearGameField()
        _gameScore.value = 0
        gameTimer.start()
    }

    private fun clearGameField() {
        _gameField.value = Array(rows) { Array(columns) {Cell.EMPTY} }
    }

    fun finishGame(){
        _isGameFinished.postValue(true)
        _time.value = 0
        clearGameField()
        stopTimer()
    }

    // todo: create class properties timer
    private fun initGameTimer(): CountDownTimer {
        _time.value = 0
        return object : CountDownTimer(GAME_TIME, GAME_TICK){
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
            override fun onFinish() { finishGame() }
        }
    }

    // creating new mole on random cell and refresh game field
    private fun generateMole(startLifeTimeMole: Long) {
        if (Random.nextFloat() >= MOLE_SPAWN_RATE) {
            val molePosColumn = Random.nextInt(0, columns)
            val molePosRow = Random.nextInt(0, rows)
            if (getCell(molePosRow, molePosColumn) != Cell.HAS_MOLE) {
                val newField = _gameField.value?.copyOf()
                newField?.get(molePosColumn)?.set(molePosRow, Cell.HAS_MOLE)
                moleQueue.offerFirst(
                    Mole(molePosColumn, molePosRow, startLifeTimeMole)
                )
                _gameField.postValue(newField!!)
            }
        }
    }

    // removing mole from game field and mole queue, using for self-destruction
    private fun removeMole(mole: Mole) {
        if (mole.col > columns || mole.row > rows) throw Exception("Cant kill mole: Invalid indices")
        val newField = _gameField.value?.copyOf() ?: getEmptyField()
        newField[mole.col][mole.row] = Cell.EMPTY
        _gameField.value = (newField)
        moleQueue.removeFirst()
    }

    fun trackUserClick(row: Int, column: Int) {
        if (isUserHitMole(row, column))
            killMole(row, column)
        Log.d(TAG, "trackUserClick: row:$row column:$column")
    }

    private fun isUserHitMole(row: Int, column: Int): Boolean {
        if ((row in 0 until rows) && (column in 0 until columns))
            if (getCell(row, column) == Cell.HAS_MOLE)
                return true
        return false
    }

    private fun getEmptyField() = Array(ROWS) { Array(COLUMNS) {Cell.EMPTY} }

    // when user tapped mole this method delete it's from queue and game field
    private fun killMole(row: Int, column: Int) {
        Log.d(TAG, "killMole")
        val field = _gameField.value?.copyOf()

        val moleToKill = moleQueue.find { mole -> mole.col == column && mole.row == row }
        if (moleToKill != null) moleQueue.remove(moleToKill)
        field?.get(column)?.set(row, Cell.EMPTY)
        _gameField.postValue(field!!)
        _gameScore.value = _gameScore.value?.plus(1)

    }

    fun stopTimer() { gameTimer.cancel() }
}