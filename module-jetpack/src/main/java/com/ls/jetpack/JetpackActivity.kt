package com.ls.jetpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.ls.jetpack.lifecycle.MyLifecycleObserver

@Route(path = "/jetpack/Index")
class JetpackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jetpack)

        Log.d("JetpackActivity", "onCreate()")
    }

    override fun onStart() {
        super.onStart()
        lifecycle.addObserver(MyLifecycleObserver())
    }
}