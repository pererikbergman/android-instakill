package com.rakangsoftware.instakill.utils

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.rakangsoftware.instakill.R

fun AppCompatActivity.loadFragment(fragment: Fragment) {
    println("AppCompatActivity:" + fragment.toString())
    supportFragmentManager
        .beginTransaction()
        .replace(
            R.id.container,
            fragment
        )
        .commit()
}

fun AppCompatActivity.showSnackbar(view: View, resourceId: Int, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, resourceId, length).show()
}