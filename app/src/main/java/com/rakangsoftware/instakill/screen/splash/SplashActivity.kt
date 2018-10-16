package com.rakangsoftware.instakill.screen.splash

import android.Manifest
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.rakangsoftware.instakill.screen.camera.CameraActivity

class SplashActivity : AppCompatActivity() {

    override fun onResume() {
        askPermissions()
        super.onResume()
    }

    private fun askPermissions() {
        askPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ) {
            CameraActivity.start(this)
            finish()
        }.onDeclined { e ->
            if (e.denied.size > 0) {
                AlertDialog.Builder(this@SplashActivity)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog: DialogInterface, which: Int ->
                        e.askAgain()
                    }
                    .setNegativeButton("no") { dialog: DialogInterface, which: Int ->
                        dialog.dismiss()
                    }.show();
            }

            if (e.foreverDenied.size > 0) {
                e.goToSettings()
            }
        }
    }

}
