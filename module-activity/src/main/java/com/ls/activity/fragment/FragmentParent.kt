package com.ls.activity.fragment

import com.ls.activity.R

class FragmentParent : BaseFragment() {

    override fun pageName(): String {
        return "parent"
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_parent_layout
    }
}