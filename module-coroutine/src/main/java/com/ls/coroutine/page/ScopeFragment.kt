package com.ls.coroutine.page

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ls.coroutine.databinding.FragmentScopeBinding
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.StringBuilder

class ScopeFragment : Fragment() {

    private lateinit var binding: FragmentScopeBinding

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScopeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        binding.btnSupervisor.setOnClickListener {
            coroutineScopeSupervisor()
        }

        binding.btnSynergy.setOnClickListener {
            coroutineScopeSynergy()
        }
    }

    /**
     * 协程作用域
     * 当一个协程A被B协程在CoroutineScope中启动时，
     * 并且协程A继承了协程B的上下文，则协程A是协程B的子协程。
     */
    private fun coroutineScopeSupervisor() {
        GlobalScope.launch {
            // 主从作用域
            supervisorScope {
                launch(CoroutineExceptionHandler { _, _ -> }) {
                    handler.post {
                        appendText("1.在主从作用域抛出异常  ArithmeticException(\"Hey!!\") \n", binding.tvDesc1)
                    }
                    throw ArithmeticException("Hey!!")
                }

                launch {
                    delay(100L)
                    handler.post {
                        appendText("2.不会影响其他子协程运行 \n", binding.tvDesc1)
                    }
                }
            }
        }
    }

    private fun coroutineScopeSynergy() {
        val exceptionHandler = CoroutineExceptionHandler {_, _ ->
            handler.post {
                appendText("5.协同作用域抛出了异常,父协程处理了异常.另外一个子协程不受影响. \n", binding.tvDesc2)
            }
        }
        GlobalScope.launch {
            try {
                coroutineScope {
                    // 协同作用域中子协程不处理抛出的异常，而是传递给父协程处理
                    try {
                        launch {
                            handler.post {
                                appendText("1.在协同作用域抛出异常  ArithmeticException(\"Hey!!\") \n", binding.tvDesc2)
                            }
                            delay(30L)
                            throw ArithmeticException("Hey!!")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        handler.post {
                            appendText("3.异常不会被捕获 \n", binding.tvDesc2)
                        }
                    }

                    launch {
                        delay(100L)
                        handler.post {
                            appendText("2.正常运行的子协程 \n", binding.tvDesc2)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {
                    appendText("4.异常被捕获,另外一个子协程被取消 \n", binding.tvDesc2)
                }
            }
        }
    }

    private fun appendText(str: String, tv: TextView) {
        val text = tv.text
        val builder = StringBuilder()
        builder.append(text.toString()).append("\n").append(str)
        tv.text = builder.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ScopeFragment()
    }
}