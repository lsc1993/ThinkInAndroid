package com.ls.coroutine.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ls.coroutine.R
import kotlinx.coroutines.*

class ScopeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scope, container, false)
    }

    /**
     * 协程作用域
     * 当一个协程A被B协程在CoroutineScope中启动时，
     * 并且协程A继承了协程B的上下文，则协程A是协程B的子协程。
     */
    private fun coroutineScope() {
        GlobalScope.launch(Dispatchers.IO) {
            // 主从作用域
            supervisorScope {
                launch(Dispatchers.Main) {
                    throw ArithmeticException("Hey!!")
                }

                MainScope().launch {
                    delay(100)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ScopeFragment()
    }
}