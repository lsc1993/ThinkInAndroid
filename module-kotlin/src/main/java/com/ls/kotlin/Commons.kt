package com.ls.kotlin

import android.app.Application
import java.util.*

/**
 * 全局可用的 Application
 */
val app: Application
    get() = AppInstance.getApp()

/**
 * 创建一个 UUID
 */
fun newUUid() = UUID.randomUUID().toString()