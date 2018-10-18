package com.rakangsoftware.instakill.utils

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
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

fun AppCompatActivity.showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, message, duration).show()
}

fun Fragment.showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, message, duration).show()
}