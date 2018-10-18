package com.rakangsoftware.instakill.screen.dashboard

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.rakangsoftware.instakill.R
import com.rakangsoftware.instakill.screen.authentication.AuthenticationActivity
import com.rakangsoftware.instakill.screen.camera.CameraFragment
import com.rakangsoftware.instakill.screen.feed.FeedFragment
import com.rakangsoftware.instakill.utils.loadFragment
import kotlinx.android.synthetic.main.camera_activity.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)

        val viewModel = ViewModelProviders.of(this).get(NavigationViewModel::class.java)
        viewModel.navigation.observe(this, Observer {
            when (it) {
                "camera" -> askPermissions()
                "feed" ->  loadFragment(FeedFragment.newInstance())
            }
        })
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Instakill"
            subtitle = "Login"
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    viewModel.launch("feed")
                    true
                }
                R.id.camera -> {
                    viewModel.launch("camera")
                    true
                }
                else -> false
            }
        }

        loadFragment(FeedFragment.newInstance())
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
            context.startActivity(Intent(context, DashboardActivity::class.java))
        }

    }
}
