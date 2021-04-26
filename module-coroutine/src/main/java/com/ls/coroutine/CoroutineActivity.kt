package com.ls.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/coroutine/Index")
class CoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)

        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.btnScope).setOnClickListener {

        }
    }
}