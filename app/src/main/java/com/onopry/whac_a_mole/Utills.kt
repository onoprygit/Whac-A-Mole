package com.onopry.whac_a_mole

import android.content.res.Resources

const val COLUMNS = 3
const val ROWS = 3
const val MOLE_SPAWN_RATE = 0.1 // milliseconds
const val MOLE_LIFE_TIME = 1000 // milliseconds

const val GAME_TIME = 10000L
const val GAME_TICK = 200L

fun Float.toDp(): Float = (this / Resources.getSystem().displayMetrics.density)
fun Float.toPx() = this * Resources.getSystem().displayMetrics.density