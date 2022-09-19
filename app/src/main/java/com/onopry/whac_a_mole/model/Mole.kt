package com.onopry.whac_a_mole.model

import com.onopry.whac_a_mole.MOLE_LIFE_TIME
import java.util.*

data class Mole(
    val col: Int,
    val row:Int,
    val startLifeTime: Long,
    val isAlive: Boolean = true
) {
    val endLifeTime = startLifeTime - MOLE_LIFE_TIME
}