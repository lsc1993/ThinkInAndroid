package com.ls.animate.propertyanimator

import android.animation.ValueAnimator
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import com.ls.animate.R
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.RuntimeException
import kotlin.coroutines.*

class PropertyAnimatorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_property_animator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val animator: ValueAnimator = ValueAnimator.ofInt(0, 100);
        animator.addUpdateListener {
            it.animatedValue
        }
        animator.start()

        val group = view.findViewById<Group>(R.id.group_1)
        group.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        test()
        //testContinue()
    }

    private fun testContinue() {
        val scope = MainScope()
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d(TAG, "Throws an exception with message: ${throwable.message}")
        }
        /*GlobalScope.launch(Dispatchers.IO + exceptionHandler) {
            supervisorScope {
                launch(Dispatchers.Main) {
                    throw ArithmeticException("Hey!!")
                }

                MainScope().launch {
                    Log.d(TAG, "hello MainScope")
                    delay(100)
                }

                withContext(Dispatchers.IO) {

                }
            }
        }*/
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                delay(100)
                Log.d(TAG, "resume +++++")
            }
        }

        /*val continuation = suspend {
            Log.d(TAG, "hello")
            //supervisor("")
            val s = supervisorScope<Int> {
                Log.d(TAG, "s +++++")
                1
            }
            Log.d(TAG, "s = $s")
            5
        }.createCoroutine(object : Continuation<Int> {
            override val context: CoroutineContext
                get() = EmptyCoroutineContext

            override fun resumeWith(result: Result<Int>) {
                Log.d(TAG, "resumeWith: ${result.getOrNull()}")
            }

        })
        continuation.resume(Unit)*/

        val list: ArrayList<String> = ArrayList()
        list.filter {
            it.isEmpty()
        }
    }

    private suspend fun supervisor(v: String) = supervisorScope {
        val a = 1 / 0
    }

    private fun test() {
        /*Single.just("Hello").subscribe()
        val d = Completable.complete().compose {
            it.retry()
        }.subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete")
            }

            override fun onError(e: Throwable) {
            }

        })

        val dd = Completable.fromAction {
            Log.d(TAG, "fromAction")
        }.subscribe()

        val publishSubject = PublishSubject.create<String>()
        val a = publishSubject.subscribe {
            Log.d(TAG, "it: $it")
        }

        publishSubject.onNext("hello")
        publishSubject.onComplete()
        publishSubject.onNext("world")*/

        val ddd = Observable.create(ObservableOnSubscribe<String> { TODO("Not yet implemented") })
            .subscribeOn(Schedulers.io())
            .map {
                Log.d(TAG, "thread:" + Thread.currentThread().name)
                "hello world"
            }
            .observeOn(Schedulers.newThread())
            .subscribe {
                Log.d(TAG, "result: $it thread: ${Thread.currentThread().name}")
            }

        val path = requireContext().filesDir
        val filepath = requireContext().getExternalFilesDir("")
        val cachePath = requireContext().externalCacheDir


        /*Log.d(TAG, "path: $path  cache: $cachePath  filepath: $filepath")

        val file1 = File(filepath!!.absolutePath + "/liu")
        if (!file1.exists()) {
            file1.mkdir()
        }*/
    }

    companion object {
        const val TAG = "PropertyAnimator"

        @JvmStatic
        fun newInstance() = PropertyAnimatorFragment()
    }
}