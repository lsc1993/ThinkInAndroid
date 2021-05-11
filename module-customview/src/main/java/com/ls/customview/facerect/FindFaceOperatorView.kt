package com.ls.customview.facerect

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 识别图片中的人脸，并根据人脸位置绘制选择人脸框
 * 这里代码只保留了逻辑框架，具体逻辑需要人脸识别能力的支持
 *
 * 1.首先确定人脸点位，计算出人脸在屏幕中的绝对位置
 * 2.绘制屏幕蒙层
 * 3.根据人脸信息计算人脸大小，绘制人脸框。
 * 利用Paint的xfermode参数，设置SRC_IN，将人脸高亮显示
 *
 * {@link https://developer.android.com/reference/android/graphics/PorterDuff.Mode}
 */
class FindFaceOperatorView : View {

    private val faceRectPaint = Paint()
    private val faceNumPaint = Paint()
    private val faceSelectPaint = Paint()
    private val faceSelectImagePaint = Paint()
    private var wrapperPaint = Paint()

    private val selectFaceImg by lazy {
        BitmapFactory.decodeResource(resources, -1)
    }

    private var faceResult = Any() // 获取人脸信息（人脸位置、点位）
    private val facePosList: ArrayList<FacePos> = ArrayList()
    private var selectFacePoint = FacePos(0f, 0f, 0f, 0f)
    private var isSelectFace = false

    private var imageWidth: Int = 0
    private var imageHeight: Int = 0
    private var scaleValue: Float = 1f
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    var selectFaceListener: OnSelectFaceListener? = null

    constructor(context: Context) : super(context) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
    }

    private fun initPaint() {
        faceRectPaint.color = Color.WHITE
        //faceRectPaint.strokeWidth = dp2px(context, 1f).toFloat()
        faceRectPaint.strokeWidth = 20f
        faceRectPaint.style = Paint.Style.STROKE
        faceRectPaint.isAntiAlias = true

        faceNumPaint.color = Color.WHITE
        faceNumPaint.style = Paint.Style.FILL
        //faceNumPaint.textSize = dp2px(context, 12f).toFloat()
        faceNumPaint.textSize = 20f
        faceNumPaint.isAntiAlias = true
        //faceNumPaint.strokeWidth = dp2px(context, 1.5f).toFloat()
        faceNumPaint.strokeWidth = 20f

        faceSelectPaint.color = Color.parseColor("#3300B272")
        faceSelectPaint.style = Paint.Style.FILL
        faceSelectPaint.isAntiAlias = true

        faceSelectImagePaint.isAntiAlias = true
        faceSelectImagePaint.style = Paint.Style.FILL
    }

    fun initFaceInfo(faceResult: Any, imageWidth: Int, imageHeight: Int, scaleValue: Float) {
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        this.scaleValue = scaleValue
        screenWidth = 1080
        screenHeight = 1920
        this.faceResult = faceResult
        if (faceResult != null) {
            val faceInfoList = ArrayList<Any>()
            for (i: Int in faceInfoList.indices) {
                val faceInfo: Any = faceInfoList[i]
                val faceRect: Rect = Rect()

                calculateFacePosInScreen(faceRect.top, faceRect.right, faceRect.bottom, faceRect.left, imageWidth, imageHeight, scaleValue)
            }
        }
    }

    /**
     * 计算人脸在屏幕的位置
     */
    private fun calculateFacePosInScreen(faceTop: Int, faceRight: Int, faceBottom: Int, faceLeft: Int, imageWidth: Int, imageHeight: Int, scaleValue: Float) {
        val widthOffset: Float = imageWidth.toFloat() / 10000 * scaleValue
        val heightOffset: Float = imageHeight.toFloat() / 10000 * scaleValue

        val screenHeight: Int = 1080
        val screenWidth: Int = 1920

        // 放大人脸框区域
        var faceTopPoint: Float = faceTop * heightOffset + top
        faceTopPoint = if (faceTopPoint / 1.2f > 0) {
            faceTopPoint / 1.2f
        } else {
            0f
        }
        val topInScreen: Float = faceTopPoint

        var faceRightPoint: Float = faceRight * widthOffset + left
        faceRightPoint = if (faceRightPoint * 1.05f <= screenWidth) {
            faceRightPoint * 1.05f
        } else {
            screenWidth.toFloat()
        }
        val rightInScreen: Float = faceRightPoint

        var faceBottomPoint: Float = faceBottom * heightOffset + top
        faceBottomPoint = if (faceBottomPoint * 1.05f <= screenHeight) {
            faceBottomPoint * 1.05f
        } else {
            screenHeight.toFloat()
        }
        val bottomInScreen: Float = faceBottomPoint

        var faceLeftPoint: Float = faceLeft * widthOffset + left
        faceLeftPoint = if (faceLeftPoint / 1.15f > 0) {
            faceLeftPoint / 1.1f
        } else {
            0f
        }
        val leftInScreen: Float = faceLeftPoint
        val facePos = FacePos(topInScreen, rightInScreen, bottomInScreen, leftInScreen)
        facePosList.add(facePos)

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var pressX = 0f
        var pressY = 0f
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressX = event.x
                pressY = event.y
            }
        }

        calculatePressPos(pressX, pressY)
        return super.onTouchEvent(event)
    }

    private fun calculatePressPos(xPos: Float, yPos: Float) {
        var pos = 0
        for (facePos: FacePos in facePosList) {
            if (xPos >= facePos.left && xPos <= facePos.right && yPos >= facePos.top && yPos <= facePos.bottom) {
                selectFacePoint = facePos
                isSelectFace = true
                invalidate()
                selectFaceListener?.onSelectFace(pos, getFaceCenterPos(pos))
                break
            }
            pos ++
        }
    }

    private fun getFaceCenterPos(pos: Int): Any {
        /*val faceInfo
        val widthOffset: Float = imageWidth.toFloat() / 10000
        val heightOffset: Float = imageHeight.toFloat() / 10000
        val top: Float = faceInfo.faceRect.top * heightOffset
        val right: Float = faceInfo.faceRect.right * widthOffset
        val bottom: Float = faceInfo.faceRect.bottom * heightOffset
        val left: Float = faceInfo.faceRect.left * widthOffset

        val imageFacePoint = ImageFacePoint()
        imageFacePoint.centerX = ((right + left) / 2).toInt()
        imageFacePoint.centerY = ((bottom + top) / 2).toInt()
        imageFacePoint.position = pos*/

        return Any()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRectWrapper(canvas)
        drawFaceRect(canvas)
        if (isSelectFace) {
            drawFaceSelectRect(canvas)
        }
    }

    private fun drawFaceRect(canvas: Canvas) {
        var faceIndex = 1
        for (facePos: FacePos in facePosList) {
            if (isFaceRect(facePos)) {
                drawFaceLineRect(canvas, facePos)
                //drawFaceNumber(canvas, facePos, faceIndex)
                faceIndex ++
            }
        }
    }

    /*private fun drawFaceNumber(canvas: Canvas, facePos: FacePos, faceIndex: Int) {
        val radius: Int = XYSizeUtils.dp2px(context, 12f)
        var circleX: Float = facePos.right
        if (radius >= screenWidth - facePos.right) {
            circleX = screenWidth - radius - 5f
        }
        var circleY: Float = facePos.bottom
        if (radius >= screenHeight - facePos.bottom) {
            circleY = screenHeight - radius - 5f
        }
        canvas.drawCircle(circleX, circleY, radius.toFloat(), faceNumCirclePaint)
        canvas.drawText(faceIndex.toString(), circleX - 7, circleY + 9, faceNumPaint)
    }*/

    /**
     * 绘制人脸框
     */
    private fun drawFaceLineRect(canvas: Canvas, facePos: FacePos) {
        canvas.drawRect(facePos.left,
                facePos.top,
                facePos.right,
                facePos.bottom,
                faceRectPaint)
    }

    /**
     * 绘制蒙层+人脸高亮显示
     */
    private fun drawRectWrapper(canvas: Canvas) {
        // 1.绘制蒙层
        wrapperPaint = Paint()
        canvas.saveLayer(0f, 0f, imageWidth * scaleValue, imageHeight * scaleValue, wrapperPaint)
        wrapperPaint.color = Color.parseColor("#88000000")
        canvas.drawRect(0f,
                0f,
                imageWidth * scaleValue,
                imageHeight * scaleValue,
                wrapperPaint)

        // 2.绘制人脸高亮显示
        for (facePos: FacePos in facePosList) {
            if (isFaceRect(facePos)) {
                wrapperPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                wrapperPaint.color = Color.TRANSPARENT
                canvas.drawRect(facePos.left,
                        facePos.top,
                        facePos.right,
                        facePos.bottom,
                        wrapperPaint)
            }
        }
    }

    private fun drawFaceSelectRect(canvas: Canvas) {
        canvas.drawRect(selectFacePoint.left,
                selectFacePoint.top,
                selectFacePoint.right,
                selectFacePoint.bottom,
                faceSelectPaint)

        val left: Float = selectFacePoint.right - 15 - selectFaceImg.width
        val top: Float = selectFacePoint.top + 15
        canvas.drawBitmap(selectFaceImg, left, top, faceSelectImagePaint)
    }

    private fun isFaceRect(facePos: FacePos): Boolean {
        return facePos.right - facePos.left > 50 && facePos.bottom - facePos.top > 50
    }

    interface OnSelectFaceListener {
        fun onSelectFace(pos: Int, facePoint: Any)
    }

    class FacePos(val top: Float, val right: Float, val bottom: Float, val left: Float)
}