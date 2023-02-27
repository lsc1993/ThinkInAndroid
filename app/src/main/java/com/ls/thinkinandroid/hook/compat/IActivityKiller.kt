package com.ls.thinkinandroid.hook.compat

import android.os.IBinder
import android.os.Message

interface IActivityKiller {

    fun finishLaunchActivity(msg: Message)

    fun finishResumeActivity(msg: Message)

    fun finishPauseActivity(msg: Message)

    fun finishStopActivity(msg: Message)

    fun finish(binder: IBinder)
}