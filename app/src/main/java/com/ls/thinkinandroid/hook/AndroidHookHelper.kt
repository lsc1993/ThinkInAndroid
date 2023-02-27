package com.ls.thinkinandroid.hook

import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import com.ls.thinkinandroid.hook.compat.ActivityKillerV28

object AndroidHookHelper {

    private const val TAG = "AndroidHookHelper"

    private const val LAUNCH_ACTIVITY = 100
    private const val PAUSE_ACTIVITY = 101
    private const val PAUSE_ACTIVITY_FINISHING = 102
    private const val STOP_ACTIVITY_HIDE = 104
    private const val RESUME_ACTIVITY = 107
    private const val DESTROY_ACTIVITY = 109
    private const val NEW_INTENT = 112
    private const val RELAUNCH_ACTIVITY = 126
    private const val EXECUTE_TRANSACTION = 159
    private const val RELAUNCH_ACTIVITY_V28 = 160

    private val activityKiller by lazy {
        ActivityKillerV28()
    }

    /**
     * hook ActivityThread$mH
     */
    fun hookMH() {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val activityThread =
            activityThreadClass.getDeclaredMethod("currentActivityThread").invoke(null)
        val mhField = activityThreadClass.getDeclaredField("mH")
        mhField.isAccessible = true
        val mhHandler = mhField.get(activityThread) as Handler
        val callbackField = Handler::class.java.getDeclaredField("mCallback")
        callbackField.isAccessible = true
        val oriCallback = callbackField.get(mhHandler) as? Handler.Callback
        callbackField.set(mhHandler, Handler.Callback { msg ->
            Log.d(TAG, "enter hook msg.what: ${msg.what}  sdk int: ${Build.VERSION.SDK_INT}")
            var result = false
            if (Build.VERSION.SDK_INT >= 28) {
                if (msg.what == EXECUTE_TRANSACTION) {
                    try {
                        Log.d(TAG, "enter EXECUTE_TRANSACTION sdk int > 28")
                        mhHandler.handleMessage(msg)
                    } catch (t: Throwable) {
                        finishExceptionActivity(msg, msg.what)
                        Log.d(TAG, "EXECUTE_TRANSACTION is error")
                    }
                    result = true
                }
            }
            when (msg.what) {
                LAUNCH_ACTIVITY, RESUME_ACTIVITY, PAUSE_ACTIVITY, PAUSE_ACTIVITY_FINISHING, STOP_ACTIVITY_HIDE, DESTROY_ACTIVITY -> {
                    try {
                        mhHandler.handleMessage(msg)
                    } catch (t: Throwable) {
                        Log.d(TAG, "hook is error, message: ${t.localizedMessage}")
                        finishExceptionActivity(msg, msg.what)
                    }
                    result = true
                }
            }
            if (oriCallback != null) {
                result = result or oriCallback.handleMessage(msg)
            }
            result
        })
    }

    private fun finishExceptionActivity(msg: Message, activityType: Int) {
        Log.d(TAG, "finishExceptionActivity activity type: $activityType")
        when (activityType) {
            LAUNCH_ACTIVITY, RELAUNCH_ACTIVITY, EXECUTE_TRANSACTION, RELAUNCH_ACTIVITY_V28 -> {
                Log.d(TAG, "finishLaunchActivity")
                activityKiller.finishLaunchActivity(msg)
            }
            PAUSE_ACTIVITY, PAUSE_ACTIVITY_FINISHING -> {
                Log.d(TAG, "finishPauseActivity")
                activityKiller.finishPauseActivity(msg)
            }
            STOP_ACTIVITY_HIDE -> {
                Log.d(TAG, "finishStopActivity")
                activityKiller.finishStopActivity(msg)
            }
            RESUME_ACTIVITY -> {
                Log.d(TAG, "finishResumeActivity")
                activityKiller.finishResumeActivity(msg)
            }
        }
    }
}