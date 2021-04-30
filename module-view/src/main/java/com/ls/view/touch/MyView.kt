package com.ls.view.touch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class MyView : AppCompatTextView {

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

  override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
    val result = super.dispatchTouchEvent(ev)
    ev?.run {
      when (ev.action) {
        MotionEvent.ACTION_DOWN -> {
          Log.d("TouchEvent", "MyViewA dispatchTouchEvent -> ACTION_DOWN")
        }
        MotionEvent.ACTION_MOVE -> {
          Log.d("TouchEvent", "MyViewA dispatchTouchEvent -> ACTION_MOVE")
        }
        MotionEvent.ACTION_UP -> {
          Log.d("TouchEvent", "MyViewA dispatchTouchEvent -> ACTION_UP")
        }
        else -> {}
      }
    }
    Log.d("TouchEvent", "MyViewA dispatchTouchEvent -> $result")
    return result
  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    val result = super.onTouchEvent(event)
    event?.run {
      when (event.action) {
        MotionEvent.ACTION_DOWN -> {
          Log.d("TouchEvent", "MyViewA onTouchEvent -> ACTION_DOWN")
          //return true
        }
        MotionEvent.ACTION_MOVE -> {
          Log.d("TouchEvent", "MyViewA onTouchEvent -> ACTION_MOVE")
          //return false
        }
        MotionEvent.ACTION_UP -> {
          Log.d("TouchEvent", "MyViewA onTouchEvent -> ACTION_UP")
          //return false
        }
        else -> {}
      }
    }
    //return result
    return false
    //return true
  }
}