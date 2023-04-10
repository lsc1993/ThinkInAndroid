package com.ls.activity.fragment

import com.ls.activity.R

class FragmentChild(private val no: Int = 0) : BaseFragment() {
    override fun pageName(): String {
        return "child-$no"
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_child_layout
    }
}