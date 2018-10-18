package com.rakangsoftware.instakill.screen.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rakangsoftware.instakill.screen.authentication.AuthenticationActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthenticationActivity.start(this)
        finish()
    }
}
