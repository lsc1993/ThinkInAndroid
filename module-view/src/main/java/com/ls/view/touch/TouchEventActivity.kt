package com.ls.view.touch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import com.ls.view.R

/**
 * dispatchTouchEvent  返回true：表示改事件在本层不再进行分发且已经在事件分发自身中被消费了。
 * dispatchTouchEvent  返回 false：表示事件在本层不再继续进行分发，并交由上层控件的onTouchEvent方法进行消费。
 *
 * onInterceptTouchEvent 返回true：表示将事件进行拦截，并将拦截到的事件交由本层控件 的onTouchEvent 进行处理。
 * onInterceptTouchEvent 返回false：表示不对事件进行拦截，事件得以成功分发到子View。
 *    并由子View的dispatchTouchEvent进行处理。
 *
 * onTouchEvent 返回 true：表示onTouchEvent处理完事件后消费了此次事件。此时事件终结，将不会进行后续的传递。
 * onTouchEvent 返回 false：事件在onTouchEvent中处理后继续向上层View传递，且有上层View的onTouchEvent进行处理。
 *
 *
 */
class TouchEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch_event)
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        val result = super.onTouchEvent(event)
//        event?.run {
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    Log.d("TouchEvent", "TouchEventActivity *** onTouchEvent -> ACTION_DOWN")
//                    //return true
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    Log.d("TouchEvent", "TouchEventActivity *** onTouchEvent -> ACTION_MOVE")
//                }
//                MotionEvent.ACTION_UP -> {
//                    Log.d("TouchEvent", "TouchEventActivity *** onTouchEvent -> ACTION_UP")
//                    //return false
//                }
//                else -> {}
//            }
//        }
//        return result
//    }
}