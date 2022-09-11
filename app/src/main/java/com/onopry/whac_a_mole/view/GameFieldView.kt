package com.onopry.whac_a_mole.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.onopry.whac_a_mole.COLUMNS
import com.onopry.whac_a_mole.R
import com.onopry.whac_a_mole.ROWS

private const val TAG = "GameFieldView_TAG"

typealias OnCellActionListener = (row: Int, column: Int) -> Unit

class GameFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var gameField: Array<Array<Boolean>>? = null
        set(value) {
            field = value
            invalidate()
        }

    var actionListener: OnCellActionListener? = null

    private var cellSize = 0f
    private var cellPadding = 0f

    private val fieldRectF = RectF(0f, 0f, 0f, 0f)

    private val bitmapCell = BitmapFactory.decodeResource(resources, R.drawable.cell)
    private val bitmapMole = BitmapFactory.decodeResource(resources, R.drawable.mole)

    private val imagePaint = Paint()
    private var defPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = resources.getColor(R.color.teal_700)
        it.style = Paint.Style.STROKE
        it.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)
    }

    init {
        if (isInEditMode) {
            gameField = arrayOf(
                arrayOf(false, true, false),
                arrayOf(true, false, false),
                arrayOf(false, true, false),
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val safeWidth = w - paddingLeft - paddingRight
        val safeHeight = h - paddingTop - paddingBottom

        val cellWidth = bitmapCell.width.toFloat()  //(safeWidth / COLUMNS).toFloat()
        val cellHeight = bitmapCell.height.toFloat() //(safeHeight / ROWS).toFloat()

        cellSize = cellWidth.coerceAtMost(cellHeight)
//        cellPadding = cellSize * 0.2f

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

//        val desiredCellSizePixels = TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP, DESIRED_CELL_SIZE, resources.displayMetrics).toInt()
        val desiredCellSizePixels = bitmapCell.width

//        val desiredMarginsBetweenCEllsPixels = TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP, MARGIN_BETWEEN_CELLS_, resources.displayMetrics).toInt()

        val desiredWidth = Integer.max(
            minWidth,
            COLUMNS * desiredCellSizePixels + paddingEnd + paddingStart/* + (desiredMarginsBetweenCEllsPixels * (COLUMNS + 1))*/
        )
        val desiredHeight = Integer.max(
            minHeight,
            ROWS * desiredCellSizePixels + paddingTop + paddingBottom /*+ (desiredMarginsBetweenCEllsPixels * (ROWS + 1))*/
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
        canvas.drawRect(fieldRectF, defPaint)
    }

    private fun drawCells(canvas: Canvas){
        gameField?.let { field ->
            for (cols in field.indices) {
                for (rows in field[cols].indices) {
                    canvas.drawBitmap(
                        bitmapCell,
                        fieldRectF.left + cols * bitmapCell.width,
//                        (fieldRectF.left + (cellPadding * cols)) + cols * bitmapCell.width,
                        fieldRectF.top + rows * bitmapCell.height,
                        imagePaint
                    )
                    if (field[cols][rows]) {
                        canvas.drawBitmap(
                            bitmapMole,
//                            (fieldRectF.left + (cellPadding * cols)) + cols * bitmapCell.width,
                            fieldRectF.left + cols * bitmapCell.width,
                            fieldRectF.top + rows * bitmapCell.height,
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
                Log.d(TAG, "onTouchEvent: {${event.x}; ${event.y}} {$column; $row}")
                if (row >= 0 && row < ROWS && column >= 0 && column < COLUMNS) {
//                    Log.d(TAG, "onTouchEvent: row=$row : column=$column")
                    actionListener?.invoke(row, column)
                    return true
                }
            }
        }
        return false
    }

    private fun getRow(event: MotionEvent) =
        ((event.y - fieldRectF.top) / bitmapCell.height).toInt()

    private fun getColumn(event: MotionEvent) =
        ((event.x - fieldRectF.left)/ bitmapCell.width).toInt()


    companion object {
        const val DESIRED_CELL_SIZE = 100f //dp
        const val MARGIN_BETWEEN_CELLS_ = 25f //dp
    }


}