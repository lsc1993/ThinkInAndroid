package com.ls.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.MotionEvent
import android.view.View
import androidx.core.util.containsKey
import kotlin.math.abs
import kotlin.math.roundToInt

class SpeedSelectorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val lineWidth = 8f
    private val lineSpace = DPUtils.dpToPixel(context, 16.0f)
    private val tallLineHeight = DPUtils.dpToPixel(context, 32.0f)
    private val shortLineHeight = DPUtils.dpToPixel(context, 20.0f)

    private val selectLinePaint = Paint()
    private val tallLinePaint = Paint()
    private val shortLinePaint = Paint()
    private val textPaint = Paint()

    // 线性马达
    private val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    // 各种标识位
    private var firstX = 0f // 第一次点击的位置
    private var lastX = 0f
    private var clickTime = 0L
    private var isMoving = false // 是否正在移动
    private var moveSpace = 0f // 每次滑动事件产生的滑动距离
    private var lineStartPos = 0f // 画线的开始位置
    private var minLineStart = 0f // 画线开始位置边界值  最小边界
    private var maxLineStart = 0f // 画线开始位置边界值  最大边界

    // 存储变速器所有的值 key - value = position - speed
    private val speedArrayMap = SparseIntArray(64)
    private val tallLinePosMap = SparseArray<String>(16).apply {
        put(0, "0.1x")
        put(4, "0.5x")
        put(9, "1x")
        put(14, "1.5x")
        put(19, "2x")
        put(24, "3x")
        put(29, "4x")
        put(34, "5x")
        put(39, "6x")
        put(44, "7x")
        put(49, "8x")
        put(54, "9x")
        put(59, "10x")
    }

    // 当前选中的变速值
    private var curSpeed = 10
    private var curSpeedPos = 9

    private var startXPos = 0f
    private var startYPos = 0f

    init {
        initPointArray()
        initPaint()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var curX = 0f
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isMoving = false
                lastX = event.x
                firstX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                isMoving = true
                curX = event.x
                moveSpace = lastX - curX
                val curTime = System.currentTimeMillis()
                if (curTime - clickTime >= VIBRATOR_DURATION) {
                    handler.post(vibratorAction)
                    clickTime = curTime
                }
                lastX = curX
                invalidate()
            }
            MotionEvent.ACTION_UP,  MotionEvent.ACTION_CANCEL -> {
                isMoving = false
                recalculateLinePos(event.x - firstX)
                //Log.d("SpeedSelectorView", "curSpeed: $curSpeed")
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        if (isMoving) {
            drawMovingLine(canvas)
        } else {
            drawCurLeftLine(canvas)
            drawCurRightLine(canvas)
        }
        drawSelectLine(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startXPos = (measuredWidth / 2).toFloat()
        startYPos = (measuredHeight + tallLineHeight) / 2
        lineStartPos = startXPos - (curSpeedPos * lineSpace)
        maxLineStart = startXPos
        minLineStart = startXPos - (59 * lineSpace)
    }

    /**
     * 绘制选中线，固定在控件中间位置
     */
    private fun drawSelectLine(canvas: Canvas) {
        val pos = curSpeedPos
        if (tallLinePosMap.containsKey(pos)) {
            canvas.drawLine(startXPos, startYPos, startXPos, startYPos - tallLineHeight, selectLinePaint)
        } else {
            canvas.drawLine(startXPos, startYPos, startXPos, startYPos - shortLineHeight, selectLinePaint)
        }
    }

    /**
     * 绘制滑动中
     */
    private fun drawMovingLine(canvas: Canvas) {
        lineStartPos -= moveSpace
        if (lineStartPos <= minLineStart) {
            lineStartPos = minLineStart
        } else if (lineStartPos >= maxLineStart) {
            lineStartPos = maxLineStart
        }
        var lineStart = lineStartPos
        for (i in 0 until 60) {
            if (tallLinePosMap.containsKey(i)) {
                drawTallLine(canvas, lineStart, i)
            } else {
                drawShortLine(canvas, lineStart)
            }
            lineStart += lineSpace
        }
    }

    /**
     * 绘制选中线左边的画线
     */
    private fun drawCurLeftLine(canvas: Canvas) {
        val curPos = curSpeedPos
        var start = startXPos
        for (i in (curPos - 1) downTo 0) {
            start -= lineSpace
            if (tallLinePosMap.containsKey(i)) {
                drawTallLine(canvas, start, i)
            } else {
                drawShortLine(canvas, start)
            }
        }
    }

    /**
     * 绘制选中线右边的画线
     */
    private fun drawCurRightLine(canvas: Canvas) {
        val curPos = curSpeedPos
        var start = startXPos
        for (i in curPos until 60) {
            if (tallLinePosMap.containsKey(i)) {
                drawTallLine(canvas, start, i)
            } else {
                drawShortLine(canvas, start)
            }
            start += lineSpace
        }
    }

    /**
     * 绘制长线
     */
    private fun drawTallLine(canvas: Canvas, start: Float, pos: Int, paint: Paint = tallLinePaint) {
        canvas.drawLine(start, startYPos, start, startYPos - tallLineHeight, paint)
        drawSpeedText(canvas, start, pos)
    }

    /**
     * 绘制短线
     */
    private fun drawShortLine(canvas: Canvas, start: Float, paint: Paint = shortLinePaint) {
        canvas.drawLine(start, startYPos, start, startYPos - shortLineHeight, paint)
    }

    /**
     * 绘制变速文字，只在长线下绘制
     */
    private fun drawSpeedText(canvas: Canvas, start: Float, pos: Int) {
        val text = tallLinePosMap.get(pos)
        val textWidth = textPaint.measureText(text)
        val textStartPos = start - textWidth / 2
        canvas.drawText(text, textStartPos, startYPos + 50, textPaint)
    }

    private fun recalculateLinePos(totalSpace: Float) {
        val moveCount = (abs(totalSpace) / lineSpace).roundToInt()
        if (totalSpace > 0) {
            curSpeedPos -= moveCount
        } else {
            curSpeedPos += moveCount
        }

        if (curSpeedPos >= 59) {
            curSpeedPos = 59
        }
        if (curSpeedPos <= 0) {
            curSpeedPos = 0
        }
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        selectLinePaint.apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.YELLOW
            strokeWidth = lineWidth
        }
        tallLinePaint.apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.BLACK
            strokeWidth = lineWidth
        }
        shortLinePaint.apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.GRAY
            strokeWidth = lineWidth
        }
        textPaint.apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.GRAY
            textSize = DPUtils.dpToPixel(context, 12).toFloat()
        }
    }

    /**
     * 初始化变速器数组，记录所有可以使用速度值
     */
    private fun initPointArray() {
        var speed = 1
        for (i in 0 until 59) {
            speedArrayMap.put(i, speed)
            speed += if (speed < 20) {
                1
            } else {
                2
            }
        }
        speedArrayMap.get(1)
    }

    /**
     * 获取当前speed值，除以10还原为原来的值
     */
    fun getCurSpeed(): Float {
        val speed = speedArrayMap.get(curSpeedPos)
        return speed.toFloat() / 10
    }

    private val vibratorAction = Runnable {
        vibrator.vibrate(VIBRATOR_DURATION)
    }

    companion object {
        const val VIBRATOR_DURATION = 100L
    }
}