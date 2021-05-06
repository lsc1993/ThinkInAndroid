package com.ls.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.alibaba.android.arouter.facade.annotation.Route
import com.ls.coroutine.page.ExceptionHandlerFragment
import com.ls.coroutine.page.ScopeFragment

@Route(path = "/coroutine/Index")
class CoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)

        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.btnScope).setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fl_container, ScopeFragment.newInstance())
            transaction.commitAllowingStateLoss()
        }

        findViewById<Button>(R.id.btnException).setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fl_container, ExceptionHandlerFragment.newInstance())
            transaction.commitAllowingStateLoss()
        }
    }
}