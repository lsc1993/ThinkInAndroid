package com.ls.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ls.activity.flag.FlagActivity
import com.ls.activity.fragment.TestFragmentActivity

/**
 * allowTaskReparenting设置为true
 * 当有和当前Activity亲和性相同的任务进入前台时
 *   当前Activity会从启动它的任务栈转移到该任务
 *
 * 启动模式的影响:
 *   standard: 当相同亲和性的App启动时,Activity处于栈顶,返回后回到MainActivity.
 *   singleTop: 当相同亲和性的App启动时,Activity处于栈顶,返回后回到MainActivity.
 *   singleTask: 当相同亲和性的App冷启动时,Activity显示到前台,返回后App直接退出.当热启动时,返回到前一个Activity.
 *   singleInstance: 当相同亲和性的App启动时,直接从根Activity启动,Activity没有显示到栈顶(因为Activity是独立的一个栈).
 */
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.btn_flag).setOnClickListener {
            val intent = Intent(this, FlagActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_fragment).setOnClickListener {
            val intent = Intent(this, TestFragmentActivity::class.java)
            startActivity(intent)
        }
    }
}