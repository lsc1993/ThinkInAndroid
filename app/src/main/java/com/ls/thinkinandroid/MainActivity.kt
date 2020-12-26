package com.ls.thinkinandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.alibaba.android.arouter.launcher.ARouter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.btnAnimate).setOnClickListener {
            ARouter.getInstance().build("/animate/Index").navigation()
        }

        findViewById<Button>(R.id.btnJetPack).setOnClickListener {
            ARouter.getInstance().build("/jetpack/Index").navigation()
        }
    }
}