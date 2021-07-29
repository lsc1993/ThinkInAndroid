package com.ls.view.custom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ls.view.R
import com.ls.view.databinding.ActivityCustomViewBinding

class CustomViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {

    }
}