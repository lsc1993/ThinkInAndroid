package com.ls.thinkinandroid.app

import android.app.Application
import android.util.Log

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //AppInstance.setApp(this)
        initARouter()
    }

    private fun initARouter() {
        Log.d("Application", "initARouter()")
    }
}