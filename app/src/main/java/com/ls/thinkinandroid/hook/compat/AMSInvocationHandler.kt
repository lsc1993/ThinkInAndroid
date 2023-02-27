package com.ls.thinkinandroid.hook.compat

import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class AMSInvocationHandler(var iamObject: Any?) : InvocationHandler {

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>): Any? {
        if ("finishActivity" == method?.name) {
            Log.d("AMSInvocationHandler", "ready to finishActivity");
            for (obj in args) {
                Log.d("AMSInvocationHandler", "invoke: object=$obj")
            }
        }
        return method?.invoke(iamObject, args);
    }
}