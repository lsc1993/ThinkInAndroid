package com.ls.activity.flag

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ls.activity.databinding.ActivityFlagBinding

class FlagActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlagBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.btnFlagSingleTask.setOnClickListener {
            startActivity(Intent(this, FlagSingleTaskActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }

        binding.btnFlagSingleClear.setOnClickListener {
            startActivity(Intent(this, FlagSingleTaskActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent . FLAG_ACTIVITY_CLEAR_TOP
            })
        }
    }
}