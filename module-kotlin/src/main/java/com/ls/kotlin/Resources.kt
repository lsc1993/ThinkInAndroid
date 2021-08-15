package com.ls.kotlin

import android.content.res.Resources
import android.graphics.drawable.Drawable

/**
 * 状态栏高度
 */
val statusHeight: Int
    get() {
        var result = 0
        try {
            val resources: Resources = app.resources
            val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        } catch (_: Exception) {
            // ignore
        }
        return result
    }

/**
 * 屏幕宽度
 */
val screenWidth = app.resources.displayMetrics.widthPixels

/**
 * 屏幕高度
 */
val screenHeight = app.resources.displayMetrics.heightPixels

/**
 * 根据 id 获取系统字符串
 */
inline fun @receiver:androidx.annotation.StringRes Int.toResString(vararg formatArgs: Any): String =
    if (formatArgs.isNullOrEmpty()) {
        app.getResString(this)
    } else {
        app.getResString(this, formatArgs)
    }


/**
 * 根据 id 获取颜色. 此方法使用的时候, 注意使用场景
 * 在深色模式切换的时候, 如果用到了 Application 获取 Color 和 Drawable 的话
 * 获取到的可能不是对应 App 模式下的资源
 */
@androidx.annotation.ColorInt
inline fun @receiver:androidx.annotation.ColorRes Int.toResColor(): Int =
    app.getResColor(this)

/**
 * 根据 id 获取 Drawable
 * 在深色模式切换的时候, 如果用到了 Application 获取 Color 和 Drawable 的话
 * 获取到的可能不是对应 App 模式下的资源
 */
inline fun @receiver:androidx.annotation.DrawableRes Int.toResDrawable(): Drawable? =
    app.getResDrawable(this)

/**
 * 根据 id 获取 Int
 */
inline fun @receiver:androidx.annotation.IntegerRes Int.toResInt(): Int =
    app.getResInt(this)

/**
 * 根据 id 获取 Bool
 */
inline fun @receiver:androidx.annotation.BoolRes Int.toResBool(): Boolean =
    app.getResBool(this)

/**
 * Int 的 dp 属性, dp to px
 */
val Int.dp
    get() = this.toFloat().dpInt

/**
 * Float 的 dp 属性, dp to px
 */
val Float.dp
    get() = app.resources.displayMetrics.density * this + 0.5f

/**
 * Float 的 dp 属性, dp to px
 */
val Float.dpInt
    get() = (this.dp + .5f).toInt() // 四舍五入