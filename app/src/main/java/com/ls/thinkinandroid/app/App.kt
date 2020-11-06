package com.ls.thinkinandroid.app

import android.app.Application
import android.util.Log
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initARouter()
    }

    private fun initARouter() {
        Log.d("Application", "initARouter()")
        if (BuildConfig.DEBUG) {
            Log.d("Application", "initARouter() open debug")
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.openDebug()
        ARouter.openLog()

        ARouter.init(this)
    }
}