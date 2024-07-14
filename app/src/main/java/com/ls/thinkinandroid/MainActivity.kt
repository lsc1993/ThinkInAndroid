package com.ls.thinkinandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ls.activity.TestActivity
import com.ls.animate.AnimateActivity
import com.ls.coroutine.CoroutineActivity
import com.ls.gallery.GalleryActivity
import com.ls.jetpack.JetpackActivity
import com.ls.media.MediaActivity
import com.ls.page.PlayerDemoActivity
import com.ls.storage.StorageActivity
import com.ls.thinkinandroid.hook.AndroidHookHelper
import com.ls.view.ViewActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        AndroidHookHelper.hookMH()
    }

    private fun initView() {
        findViewById<Button>(R.id.btnAnimate).setOnClickListener {
            startActivity(Intent(this, AnimateActivity::class.java))
        }

        findViewById<Button>(R.id.btnJetPack).setOnClickListener {
            startActivity(Intent(this, JetpackActivity::class.java))
        }

        findViewById<Button>(R.id.btnMedia).setOnClickListener {
            startActivity(Intent(this, MediaActivity::class.java))
        }

        findViewById<Button>(R.id.btnPlayer).setOnClickListener {
            startActivity(Intent(this, PlayerDemoActivity::class.java))
        }

        findViewById<Button>(R.id.btnCoroutine).setOnClickListener {
            startActivity(Intent(this, CoroutineActivity::class.java))
        }

        findViewById<Button>(R.id.btnActivity).setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }

        findViewById<Button>(R.id.btnView).setOnClickListener {
            startActivity(Intent(this, ViewActivity::class.java))
        }

        findViewById<Button>(R.id.btnGallery).setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }

        findViewById<Button>(R.id.btnStorage).setOnClickListener {
            startActivity(Intent(this, StorageActivity::class.java))
        }
    }
}