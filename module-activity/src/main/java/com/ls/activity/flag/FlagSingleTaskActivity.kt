package com.ls.activity.flag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ls.activity.R
import com.ls.activity.databinding.ActivityFlagSingleTaskBinding
import java.lang.StringBuilder

class FlagSingleTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlagSingleTaskBinding
    private var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlagSingleTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        Log.d("TestFlag", "onCreate()")
        appendText("onCreate()")
    }

    private fun initView() {
        binding.btnStartPre.setOnClickListener {
            startActivity(Intent(this, FlagActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("TestFlag", "onStart()")
        appendText("onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TestFlag", "onResume()")
        appendText("onResume()")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("TestFlag", "onNewIntent()")
        appendText("onNewIntent()")
    }

    private fun appendText(str: String) {
        val text = binding.tvDesc.text
        val builder = StringBuilder()
        builder.append(text.toString()).append("\n").append(count).append(".").append(str).append("\n")
        binding.tvDesc.text = builder.toString()
        count ++
    }
}