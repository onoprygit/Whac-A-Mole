package com.onopry.whac_a_mole.model

import com.onopry.whac_a_mole.MOLE_LIFE_TIME
import java.util.*

class MoleLifeSpace(){
    private val moleLifeQueue: Queue<Mole> = LinkedList()
    private val molesSet = mutableSetOf<Mole>()

    fun addMole(molePosColumn: Int, molePosRow: Int, startLifeTimeMole: Long){
        val mole = Mole(molePosColumn, molePosRow, startLifeTimeMole)
        molesSet.add(mole)
        moleLifeQueue.offer(mole)
    }

    fun removeMole(mole: Mole) {
        //molesSet.first { it.isAlive }.isAlive = false
        moleLifeQueue.remove()
    }
}

data class Mole(
    val col: Int,
    val row:Int,
    val startLifeTime: Long,
    val isAlive: Boolean = true
) {
    val endLifeTime = startLifeTime - MOLE_LIFE_TIME
}