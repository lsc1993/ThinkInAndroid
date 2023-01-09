package com.ls.kotlin

import android.app.Application

object AppInstance {

    internal var _app: Application? = null

    var isDebug: Boolean = false

    fun getApp(): Application {
        return _app!!
    }

    fun setApp(app: Application) {
        _app = app
    }

}