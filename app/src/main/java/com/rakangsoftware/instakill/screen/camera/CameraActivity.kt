package com.rakangsoftware.instakill.screen.camera

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.tasks.Task
import com.rakangsoftware.instakill.R
import com.rakangsoftware.instakill.screen.authentication.AuthenticationActivity
import com.rakangsoftware.instakill.utils.loadFragment

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Instakill"
            subtitle = "Login"
        }
    }

    override fun onResume() {
        super.onResume()
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
                AlertDialog.Builder(this@CameraActivity)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> logout()
        }
        return true
    }

    fun logout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                AuthenticationActivity.start(this)
                finish()
            }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CameraActivity::class.java))
        }

    }
}
