package com.ls.activity.fragment

import com.ls.activity.R

class FragmentChild : BaseFragment() {
    override fun pageName(): String {
        return "child"
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_child_layout
    }
}