package com.rakangsoftware.instakill.screen.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.rakangsoftware.instakill.R
import com.rakangsoftware.instakill.screen.camera.CameraActivity
import com.rakangsoftware.instakill.utils.showSnackbar
import kotlinx.android.synthetic.main.authentication_activity.*
import java.util.*

class AuthenticationActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Instakill"
            subtitle = "Login"
        }

        sign_in_button.isEnabled = false
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            goCamera()
        } else {
            sign_in_button.isEnabled = true
            sign_in_button.setOnClickListener {
                signInGoogle()
            }
        }

    }

    private fun signInGoogle() {
        // not signed in
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false, true)
                .setAvailableProviders(
                    Arrays.asList(
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )
                )
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                goCamera()
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(root_view, R.string.sign_in_cancelled)
                    return
                }

                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    showSnackbar(root_view, R.string.no_internet_connection)
                    return
                }

                showSnackbar(root_view, R.string.unknown_error)
            }
        }
    }

    private fun goCamera() {
        CameraActivity.start(this)
        finish()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AuthenticationActivity::class.java))
        }
    }
}
