package com.ls.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ls.view.custom.CustomViewActivity
import com.ls.view.databinding.ActivityViewBinding
import com.ls.view.touch.TouchEventActivity

@Route(path = "/view/Index")
class ViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.btnTouchEvent.setOnClickListener {
            startActivity(Intent(this, TouchEventActivity::class.java))
        }

        binding.btnCustomView.setOnClickListener {
            startActivity(Intent(this, CustomViewActivity::class.java))
        }
    }
}