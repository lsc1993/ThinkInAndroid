package com.ls.view.touch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.RelativeLayout

class MyGroupA : RelativeLayout {

  constructor(context: Context) : super(context) {
  }

  constructor(
    context: Context,
    attrs: AttributeSet?
  ) : super(context, attrs) {
  }

  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr) {
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    if (childCount > 0) {
      //只布局第一个child
      val child = getChildAt (0);
      child.layout(0, 0, child.measuredWidth, child.measuredHeight);
    }
  }

  override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
    val result = super.dispatchTouchEvent(ev)
    ev?.run {
      when (ev.action) {
        MotionEvent.ACTION_DOWN -> {
          Log.d("TouchEvent", "MyGroupA ### dispatchTouchEvent -> ACTION_DOWN")
        }
        MotionEvent.ACTION_MOVE -> {
          Log.d("TouchEvent", "MyGroupA ### dispatchTouchEvent -> ACTION_MOVE")
          //return true
        }
        MotionEvent.ACTION_UP -> {
          Log.d("TouchEvent", "MyGroupA ### dispatchTouchEvent -> ACTION_UP")
        }
        else -> {
        }
      }
    }
    Log.d("TouchEvent", "MyGroupA ### dispatchTouchEvent -> $result")
    return result
  }

  override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
    super.onInterceptTouchEvent(ev)
    ev?.run {
      when (ev.action) {
        MotionEvent.ACTION_DOWN -> {
          Log.d("TouchEvent", "MyGroupA ### onInterceptTouchEvent -> ACTION_DOWN")
        }
        MotionEvent.ACTION_MOVE -> {
          Log.d("TouchEvent", "MyGroupA ### onInterceptTouchEvent -> ACTION_MOVE")
          return true
        }
        MotionEvent.ACTION_UP -> {
          Log.d("TouchEvent", "MyGroupA ### onInterceptTouchEvent -> ACTION_UP")
        }
        else -> {
        }
      }
    }
    return false
  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    event?.run {
      when (event.action) {
        MotionEvent.ACTION_DOWN -> {
          Log.d("TouchEvent", "MyGroupA ### onTouchEvent -> ACTION_DOWN")
        }
        MotionEvent.ACTION_MOVE -> {
          Log.d("TouchEvent", "MyGroupA ### onTouchEvent -> ACTION_MOVE")
        }
        MotionEvent.ACTION_UP -> {
          Log.d("TouchEvent", "MyGroupA ### onTouchEvent -> ACTION_UP")
        }
        else -> {
        }
      }
    }
    return super.onTouchEvent(event)
  }
}