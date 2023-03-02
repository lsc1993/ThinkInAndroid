package com.ls.coroutine.page

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ls.coroutine.R
import com.ls.coroutine.databinding.FragmentExceptionHandlerBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import java.lang.StringBuilder

class ExceptionHandlerFragment : Fragment() {

    private lateinit var binding: FragmentExceptionHandlerBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExceptionHandlerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        binding.btnException.setOnClickListener {
            throwException()
        }
    }

    private fun throwException() {
        val exceptionHandler = CoroutineExceptionHandler {_, _ ->
            handler.post {
                appendText("已通过CoroutineExceptionHandler捕获异常\n", binding.tvDesc1)
            }
        }

        val exceptionHandler1 = CoroutineExceptionHandler {_, _ ->
            handler.post {
                appendText("已通过CoroutineExceptionHandler1----捕获异常\n", binding.tvDesc1)
            }
        }

        GlobalScope.launch(exceptionHandler) {
            /*launch {
                handler.post {
                    appendText("子协程抛出了异常\n", binding.tvDesc1)
                }
                throw ArithmeticException("Hey!!")
            }*/

            val result = async { "" }
            result.await()

            val resultList = listOf(
                async {
                      withContext(Dispatchers.IO) {

                      }
                },
                async {  }
            )

            flow<Int> {
                listOf<Int>(1, 2, 3).forEach {
                    emit(1)
                }
            }.collect {  }
            resultList.awaitAll()

            /**
             * 这里CoroutineExceptionHandler无法捕获子协程异常
             */
            launch(exceptionHandler1) {
                launch {
                    handler.post {
                        appendText("子子协程抛出了异常\n", binding.tvDesc1)
                    }
                    //子子协程抛出异常
                    throw ArithmeticException("Hey!!")
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
        fun newInstance() = ExceptionHandlerFragment()
    }
}