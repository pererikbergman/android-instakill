package com.rakangsoftware.instakill.screen.dashboard

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.rakangsoftware.instakill.R
import com.rakangsoftware.instakill.screen.camera.CameraFragment
import com.rakangsoftware.instakill.utils.loadFragment

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)

        loadCamera()
    }

    private fun loadCamera() {
        askPermissions()
    }

    private fun askPermissions() {
        askPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ) {
            loadFragment(CameraFragment.newInstance())
        }.onDeclined { e ->
            if (e.denied.size > 0) {
                AlertDialog.Builder(this@DashboardActivity)
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

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DashboardActivity::class.java))
        }

    }
}
