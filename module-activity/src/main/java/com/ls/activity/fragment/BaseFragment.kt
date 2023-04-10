package com.ls.activity.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG + pageName(), "onAttach =====")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG + pageName(), "onCreate =====")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG + pageName(), "onActivityCreated =====")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG + pageName(), "onCreateView =====")
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG + pageName(), "onViewCreated =====")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG + pageName(), "onResume =====")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d(TAG + pageName(), "onHiddenChanged hidden: $hidden")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG + pageName(), "onPause =====")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG + pageName(), "onDestroyView =====")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG + pageName(), "onDestroy =====")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG + pageName(), "onDetach =====")
    }

    abstract fun pageName(): String

    abstract fun getLayoutRes(): Int

    companion object {
        const val TAG = "Fragment-"
    }
}