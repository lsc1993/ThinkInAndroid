package com.ls.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ls.player.databinding.ActivityPlayerDemoBinding

@Route(path = "/player/Index")
class PlayerDemoActivity : AppCompatActivity() {

    private lateinit var root: ActivityPlayerDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = ActivityPlayerDemoBinding.inflate(layoutInflater)
        setContentView(root.root)

        initView()
    }

    private fun initView() {
        root.btnJustPlay.setOnClickListener {

        }

        root.btnOpenVideo.setOnClickListener {
            val intent = Intent(this@PlayerDemoActivity, PlayVideoActivity::class.java)
            startActivity(intent)
        }

        root.btnListVideo.setOnClickListener {

        }

        root.btnRecyclerVideo.setOnClickListener {

        }
    }
}