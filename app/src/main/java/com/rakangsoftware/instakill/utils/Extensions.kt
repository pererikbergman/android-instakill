package com.rakangsoftware.instakill.utils

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.rakangsoftware.instakill.R

fun AppCompatActivity.loadFragment(fragment: Fragment) {
    supportFragmentManager
            .beginTransaction()
            .replace(
                    R.id.container,
                    fragment
            )
            .commit()
}