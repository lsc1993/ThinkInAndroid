package com.ls.thinkinandroid.hook.compat

import android.app.ActivityManager
import android.content.Intent
import android.os.IBinder
import android.os.Message
import android.util.Log
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * 通过hook AMS实现finish Activity
 */
class ActivityKillerV28 : IActivityKiller {
    override fun finishLaunchActivity(msg: Message) {
        try {
            Log.d("ActivityKillerV28", "tryFinish1")
            tryFinish1(msg)
        } catch (t: Throwable) {
            try {
                Log.d("ActivityKillerV28", "tryFinish2")
                tryFinish2(msg)
            } catch (t2: Throwable) {
                try {
                    Log.d("ActivityKillerV28", "tryFinish3")
                    tryFinish3(msg)
                } catch (t3: Throwable) {
                    Log.d("ActivityKillerV28", "tryFinish3 error ")
                }
            }
        }
    }

    override fun finishResumeActivity(msg: Message) {

    }

    override fun finishPauseActivity(msg: Message) {

    }

    override fun finishStopActivity(msg: Message) {

    }

    fun finishA() {
        // 第一步：获取 IActivityManagerSingleton
        val aClass = Class.forName("android.app.ActivityManager")
        val declaredField = aClass.getDeclaredField("IActivityManagerSingleton")
        declaredField.isAccessible = true
        val value = declaredField.get(null)

        val singletonClz = Class.forName("android.util.Singleton")
        val instanceField = singletonClz.getDeclaredField("mInstance")
        instanceField.isAccessible = true
        val iActivityManagerObject = instanceField.get(value)

        // 第二步：获取我们的代理对象，这里因为 IActivityManager 是接口，我们使用动态代理的方式
        val iActivity = Class.forName("android.app.IActivityManager");
        val handler = AMSInvocationHandler(iActivityManagerObject)
        val proxy = Proxy.newProxyInstance(
            Thread.currentThread().contextClassLoader,
            arrayOf(iActivity),
            handler
        )
        // 第三步：偷梁换柱，将我们的 proxy 替换原来的对象
        instanceField.set(value, proxy)
    }

    override fun finish(binder: IBinder) {
        val getServiceMethod = ActivityManager::class.java.getMethod("getService")
        // 获取AMS代理实例
        val activityManager = getServiceMethod.invoke(null)
        // 获取finishActivity方法
        val finishActivityMethod = activityManager.javaClass.getMethod(
            "finishActivity",
            IBinder::class.java, Integer.TYPE,
            Intent::class.java, Integer.TYPE
        )
        val finishActivityFlag = 0
        finishActivityMethod.invoke(
            activityManager,
            binder,
            0,
            null,
            Integer.valueOf(finishActivityFlag)
        )
    }

    @Throws(Throwable::class)
    private fun tryFinish1(msg: Message) {
        val clientTransaction = msg.obj
        val mActivityTokenFiled = clientTransaction.javaClass.getDeclaredMethod("getActivityToken")
        val binder = mActivityTokenFiled.invoke(clientTransaction) as IBinder
        finish(binder)
    }

    @Throws(Throwable::class)
    private fun tryFinish2(message: Message) {
        val clientTransaction = message.obj
        val mActivityTokenField: Field =
            clientTransaction::class.java.getDeclaredField("mActivityToken")
        mActivityTokenField.isAccessible = true
        val binder = mActivityTokenField.get(clientTransaction) as IBinder
        finish(binder)
    }

    @Throws(Throwable::class)
    private fun tryFinish3(message: Message) {
        val clientTransaction = message.obj
        val metaGetDeclaredMethod: Method = Class::class.java.getDeclaredMethod(
            "getDeclaredMethod",
            String::class.java,
            Array::class.java
        )
        val getActivityTokenMethod: Method = metaGetDeclaredMethod.invoke(
            clientTransaction.javaClass,
            "getActivityToken",
            null
        ) as Method
        val binder = getActivityTokenMethod.invoke(clientTransaction) as IBinder
        finish(binder)
    }
}