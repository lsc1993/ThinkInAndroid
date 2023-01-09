package com.ls.thinkinandroid.app

import android.app.Application
import android.util.Log
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import com.ls.kotlin.AppInstance

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //AppInstance.setApp(this)
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