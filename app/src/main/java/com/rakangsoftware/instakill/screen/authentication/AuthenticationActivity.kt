package com.rakangsoftware.instakill.screen.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rakangsoftware.instakill.R
import com.rakangsoftware.instakill.screen.dashboard.DashboardActivity
import com.rakangsoftware.instakill.utils.showSnackbar
import kotlinx.android.synthetic.main.authentication_activity.*
import java.util.*

class AuthenticationActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 62342

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication_activity)

        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            // Logged in !
            DashboardActivity.start(this)
            finish()
        } ?: run {
            // Not Logged in!
            signInGoogle.setOnClickListener {
                signInGoogle()
            }
        }
    }

    private fun signInGoogle() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
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
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Logged in!!!
                DashboardActivity.start(this)
                finish()
            } else {
                if (response == null) {
                    showSnackbar(root_view, getString(R.string.sign_in_cancelled)) // User pressed back button
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    showSnackbar(root_view, getString(R.string.no_internet_connection))
                    return
                }

                println("response.error.localizedMessage: " + response.error?.cause)
                showSnackbar(root_view, getString(R.string.unknown_error))
            }
        }
    }


    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AuthenticationActivity::class.java))
        }
    }
}
