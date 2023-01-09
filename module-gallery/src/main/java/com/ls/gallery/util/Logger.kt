package com.ls.gallery.util

import android.util.Log

object Logger {

    private val isDebug = true

    private val keyMap = HashMap<String, Long>()

    fun d(tag: String, msg: String) {
        if (isDebug) {
            Log.d(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (isDebug) {
            Log.e(tag, msg)
        }
    }

    /**
     * 用于统计方法耗时
     */
    fun start(tag: String, msg: String, key: String) {
        if (isDebug) {
            val startTime = System.currentTimeMillis()
            val content = "start at: $startTime, mag: $msg"
            keyMap[key] = startTime
            Log.d(tag, content)
        }
    }

    /**
     * 用于统计方法耗时
     */
    fun end(tag: String, msg: String, key: String) {
        if (isDebug) {
            val endTime = System.currentTimeMillis()
            val startTime = keyMap[key]
            if (startTime != null) {
                val content = "cost time: ${endTime - startTime} ms, mag: $msg"
                Log.d(tag, content)
            }
        }
    }
}