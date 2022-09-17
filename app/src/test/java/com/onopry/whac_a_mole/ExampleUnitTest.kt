package com.onopry.whac_a_mole

import com.onopry.whac_a_mole.model.Cell
import com.onopry.whac_a_mole.model.WhacAMoleGame
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {



    @Test
    fun `is get cells return valid values`() {
        val test_model = WhacAMoleGame(3, 3)

        val outMoreCellTest = test_model.getCell(5, 5)
        val outLessCellTest = test_model.getCell(-1, -1)
        val minLimitCellValueTest = test_model.getCell(0, 0)
        val maxLimitCellValueTest = test_model.getCell(3, 3)
        val fifthCellTest = test_model.getCell(2, 2)

        assertEquals(Cell.EMPTY, outMoreCellTest)
        assertEquals(Cell.EMPTY, outLessCellTest)
        assertEquals(Cell.EMPTY, maxLimitCellValueTest)
    }
}