package com.ls.thinkinandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.alibaba.android.arouter.launcher.ARouter
import com.ls.thinkinandroid.hook.AndroidHookHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        AndroidHookHelper.hookMH()
    }

    private fun initView() {
        findViewById<Button>(R.id.btnAnimate).setOnClickListener {
            ARouter.getInstance().build("/animate/Index").navigation()
        }

        findViewById<Button>(R.id.btnJetPack).setOnClickListener {
            ARouter.getInstance().build("/jetpack/Index").navigation()
        }

        findViewById<Button>(R.id.btnMedia).setOnClickListener {
            ARouter.getInstance().build("/media/Index").navigation()
        }

        findViewById<Button>(R.id.btnPlayer).setOnClickListener {
            ARouter.getInstance().build("/player/Index").navigation()
        }

        findViewById<Button>(R.id.btnCoroutine).setOnClickListener {
            ARouter.getInstance().build("/coroutine/Index").navigation()
        }

        findViewById<Button>(R.id.btnActivity).setOnClickListener {
            ARouter.getInstance().build("/activity/Index").navigation()
        }

        findViewById<Button>(R.id.btnView).setOnClickListener {
            ARouter.getInstance().build("/view/Index").navigation()
        }

        findViewById<Button>(R.id.btnGallery).setOnClickListener {
            ARouter.getInstance().build("/gallery/Index").navigation()
        }

        findViewById<Button>(R.id.btnStorage).setOnClickListener {
            ARouter.getInstance().build("/storage/Index").navigation()
        }
    }
}