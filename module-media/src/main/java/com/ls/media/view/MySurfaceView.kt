package com.ls.media.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.ls.media.R
import java.lang.Exception
import kotlin.random.Random

class MySurfaceView : SurfaceView, Runnable {

    private lateinit var canvas: Canvas
    private val random: Random = Random(System.currentTimeMillis())
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val thread: Thread = Thread(this)

    private var isRun = true

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#F29E1F")

        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                isRun = true
                thread.start()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                isRun = false
                thread.interrupt()
            }

        })
    }

    private fun draw() {
        canvas = holder.lockCanvas()
        canvas.drawColor(resources.getColor(R.color.design_default_color_error), PorterDuff.Mode.SRC_OVER)
        val x = random.nextFloat() * 500
        val y = random.nextFloat() * 500
        canvas.drawCircle(x, y, 20f, paint)
        holder.unlockCanvasAndPost(canvas)
    }

    override fun run() {
        try {
            while (isRun) {
                draw()
                Thread.sleep(100L)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}