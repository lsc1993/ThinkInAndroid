package com.ls.animate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ls.animate.propertyanimator.PropertyAnimatorFragment

class AnimateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animate)

        setFragment()
    }

    private fun setFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_container, PropertyAnimatorFragment.newInstance(), "PropertyAnimatorFragment")
        transaction.commit()
    }
}