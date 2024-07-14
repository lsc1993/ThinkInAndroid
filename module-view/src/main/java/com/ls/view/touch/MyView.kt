package com.ls.view.touch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class MyView : View {

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
    ev?.run {
      when (ev.action) {
        MotionEvent.ACTION_DOWN -> {
          Log.d("TouchEvent", "MyViewA dispatchTouchEvent -> ACTION_DOWN")
        }
        MotionEvent.ACTION_MOVE -> {
          Log.d("TouchEvent", "MyViewA dispatchTouchEvent -> ACTION_MOVE")
//          return false
        }
        MotionEvent.ACTION_UP -> {
          Log.d("TouchEvent", "MyViewA dispatchTouchEvent -> ACTION_UP")
        }
        else -> {}
      }
    }
    val result = super.dispatchTouchEvent(ev)
    Log.d("TouchEvent", "MyViewA dispatchTouchEvent -> $result")
    return result
  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    event?.run {
      when (event.action) {
        MotionEvent.ACTION_DOWN -> {
          Log.d("TouchEvent", "MyViewA onTouchEvent -> ACTION_DOWN")
          //return true
        }
        MotionEvent.ACTION_MOVE -> {
          Log.d("TouchEvent", "MyViewA onTouchEvent -> ACTION_MOVE")
//          return false
        }
        MotionEvent.ACTION_UP -> {
          Log.d("TouchEvent", "MyViewA onTouchEvent -> ACTION_UP")
          //return false
        }
        MotionEvent.ACTION_CANCEL -> {
          Log.d("TouchEvent", "MyViewA onTouchEvent -> ACTION_CANCEL")
        }
        else -> {}
      }
    }
    val result = super.onTouchEvent(event)
    Log.d("TouchEvent", "MyViewA ### onTouchEvent -> $result")
    return result
    //return false
    //return true
  }
}