package com.rakangsoftware.instakill.screen.splash

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.rakangsoftware.instakill.screen.dashboard.DashboardActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DashboardActivity.start(this)
        finish()
    }
}
