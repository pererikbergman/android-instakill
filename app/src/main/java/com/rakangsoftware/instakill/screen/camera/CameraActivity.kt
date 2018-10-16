package com.rakangsoftware.instakill.screen.camera

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rakangsoftware.instakill.R
import com.rakangsoftware.instakill.utils.loadFragment

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)

        loadFragment(CameraFragment.newInstance())
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CameraActivity::class.java))
        }

    }
}
