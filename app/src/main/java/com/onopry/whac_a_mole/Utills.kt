package com.onopry.whac_a_mole

import android.content.res.Resources

const val COLUMNS = 3
const val ROWS = 3
const val MOLE_SPAWN_RATE = 0.1 // milliseconds
const val MOLE_LIFE_TIME = 1000 // milliseconds

const val GAME_TIME = 100000L
const val GAME_TICK = 200L

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx() = this * Resources.getSystem().displayMetrics.density