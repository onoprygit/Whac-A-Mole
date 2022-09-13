package com.onopry.whac_a_mole.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.onopry.whac_a_mole.*
import com.onopry.whac_a_mole.model.Cell
import kotlin.math.floor

private const val TAG = "GameFieldView_TAG"

typealias OnCellActionListener = (row: Int, column: Int) -> Unit

class GameFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var gameField: Array<Array<Cell>>? = null
        set(value) {
            field = value
            invalidate()
        }

    var actionListener: OnCellActionListener? = null

    private var cellSize = 0f
    private val fieldRectF = RectF(0f, 0f, 0f, 0f)

    private val imagePaint = Paint()

    private var cellBitmap: Bitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.ic_cell),
        DESIRED_CELL_SIZE_DP.toPx().toInt(),
        ((DESIRED_CELL_SIZE_DP / 0.88f).toPx()).toInt(),
        true
    )

    private val moleBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.ic_mole),
        DESIRED_CELL_SIZE_DP.toPx().toInt(),
        ((DESIRED_CELL_SIZE_DP / 0.88f).toPx()).toInt(),
        true
    )

    private val fistBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.ic_hammer),
        DESIRED_FIST_SIZE_DP.toPx().toInt(),
        DESIRED_FIST_SIZE_DP.toPx().toInt(),
        true
    )

    init {
        if (isInEditMode) {
            gameField = arrayOf(
                arrayOf(Cell.EMPTY, Cell.HAS_MOLE, Cell.EMPTY),
                arrayOf(Cell.HAS_MOLE, Cell.EMPTY, Cell.EMPTY),
                arrayOf(Cell.EMPTY, Cell.HAS_MOLE, Cell.EMPTY),
            )
        }
        Log.d(TAG, "png size pixels: ${cellBitmap.height}; dp: ${cellBitmap.height.toFloat().toDp()} ")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val safeWidth = w - paddingLeft - paddingRight
        val safeHeight = h - paddingTop - paddingBottom

        val cellWidth = cellBitmap.width.toFloat()
        val cellHeight = cellBitmap.height.toFloat()

        cellSize = cellWidth.coerceAtMost(cellHeight)

        val fieldWidth = cellSize * COLUMNS - 1
        val fieldHeight = cellSize * ROWS - 1

        fieldRectF.left = paddingLeft + (safeWidth - fieldWidth) / 2
        fieldRectF.top = paddingTop + (safeHeight - fieldHeight) / 2
        fieldRectF.right = fieldRectF.left + fieldWidth
        fieldRectF.bottom = fieldRectF.top + fieldHeight

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingStart + paddingEnd
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredCellSizePixels = cellBitmap.width

        val desiredWidth = Integer.max(
            minWidth,
            COLUMNS * desiredCellSizePixels + paddingEnd + paddingStart
        )
        val desiredHeight = Integer.max(
            minHeight,
            ROWS * desiredCellSizePixels + paddingTop + paddingBottom
        )

        setMeasuredDimension(
            resolveSize(widthMeasureSpec, desiredWidth),
            resolveSize(heightMeasureSpec, desiredHeight)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        if (gameField != null) return
//        if (fieldRectF.width() <= 0) return
//        if (fieldRectF.height() <= 0) return
        drawCells(canvas)
    }

    private fun drawCells(canvas: Canvas){
        gameField?.let { field ->
            for (cols in field.indices) {
                for (rows in field[cols].indices) {
                    canvas.drawBitmap(
                        cellBitmap,
                        fieldRectF.left + cols * cellBitmap.width,
                        fieldRectF.top + rows * cellBitmap.height,
                        imagePaint
                    )
//                  todo model in view!!!
                    if (field[cols][rows] == Cell.HAS_MOLE) {
                        canvas.drawBitmap(
                            moleBitmap,
                            fieldRectF.left + cols * cellBitmap.width,
                            fieldRectF.top + rows * cellBitmap.height,
                            imagePaint
                        )
                    }
                }
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                val row = getRow(event)
                val column = getColumn(event)
                actionListener?.invoke(row, column)
                return true
            }
        }
        return false
    }

    private fun getRow(event: MotionEvent) =
        floor(((event.y - fieldRectF.top) / cellBitmap.height)).toInt()

    private fun getColumn(event: MotionEvent) =
        floor(((event.x - fieldRectF.left)/ cellBitmap.width)).toInt()


    companion object {
        const val DESIRED_CELL_SIZE_DP = 115f
        const val DESIRED_FIST_SIZE_DP = 50f
    }


}