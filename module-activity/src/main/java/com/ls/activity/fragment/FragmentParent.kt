package com.ls.activity.fragment

import android.os.Bundle
import android.view.View
import com.ls.activity.R

class FragmentParent : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.beginTransaction()
            .add(R.id.fl_child_container, FragmentChild(1), "child1")
            .addToBackStack("child")
            .setReorderingAllowed(true)
//            .add(R.id.fl_child_container, FragmentChild(2), "child2")
//            .addToBackStack("child")
            .commitAllowingStateLoss()

        parentFragmentManager.beginTransaction()
//            .add(R.id.fl_child_container, FragmentChild(1), "child1")
//            .addToBackStack("child")
            .add(R.id.fl_child_container, FragmentChild(2), "child2")
            .addToBackStack("child")
            .setReorderingAllowed(true)
            .commitAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()
        //childFragmentManager.restoreBackStack("child")
    }

    override fun pageName(): String {
        return "parent"
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_parent_layout
    }
}