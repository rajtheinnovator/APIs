package com.enpassio.apis

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


class RestApiActivity : AppCompatActivity() {


    private val TAG = RestApiActivity::class.java.simpleName
    private val RC_GET_TOKEN = 9002

    private var mGoogleSignInClient: GoogleSignInClient? = null
    lateinit var mIdTokenTextView: TextView
    lateinit var mRefreshButton: Button

    // Button click listeners
    lateinit var signIn: Button
    lateinit var signOut: Button
    lateinit var disconnect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_api)

        mRefreshButton = findViewById(R.id.button_refresh_id)
        mIdTokenTextView = findViewById(R.id.text_view_value_token)


        // Button click listeners
        signIn = findViewById(R.id.button_sign_in)
        signIn.setOnClickListener { startSignIn() }
        signOut = findViewById(R.id.button_sign_out)
        signOut.setOnClickListener { startSignOut() }
        disconnect = findViewById(R.id.button_disconnect)
        disconnect.setOnClickListener { startDisconnection() }
        mRefreshButton.setOnClickListener { refresh() }


        // [START configure_signin]
        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_API_CLIENT_ID)
                .requestEmail()
                .build()
        // [END configure_signin]

        // Build GoogleAPIClient with the Google Sign-In API and the above options.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    // [START handle_sign_in_result]
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account.idToken

            // TODO(developer): send ID Token to server and validate

            updateUI(account)
        } catch (e: ApiException) {
            Log.e(TAG, "handleSignInResult:error", e)
            updateUI(null)
        }

    }

    // [END handle_sign_in_result]
    private fun startDisconnection() {
        mGoogleSignInClient!!.revokeAccess().addOnCompleteListener(this,
                object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        updateUI(null)
                    }
                })
    }

    private fun startSignOut() {
        mGoogleSignInClient!!.revokeAccess().addOnCompleteListener(this,
                object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        updateUI(null)
                    }
                })
    }

    private fun startSignIn() {
        // Show an account picker to let the user choose a Google account from the device.
        // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
        // consent screen will be shown here.
        val signInIntent = mGoogleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, RC_GET_TOKEN)
    }


    private fun refresh() {
        // Attempt to silently refresh the GoogleSignInAccount. If the GoogleSignInAccount
        // already has a valid token this method may complete immediately.
        //
        // If the user has not previously signed in on this device or the sign-in has expired,
        // this asynchronous branch will attempt to sign in the user silently and get a valid
        // ID token. Cross-device single sign on will occur in this branch.
        mGoogleSignInClient?.silentSignIn()
                ?.addOnCompleteListener(this) { task -> handleSignInResult(task) }
                ?.addOnCompleteListener(this) { task -> handleSignInResult(task) }

    }

    private fun updateUI(@Nullable account: GoogleSignInAccount?) {
        if (account != null) {
            val idToken = account.idToken
            mIdTokenTextView.text = idToken

            signIn.visibility = View.GONE
            signOut.visibility = View.VISIBLE
            mRefreshButton.visibility = View.VISIBLE
        } else {
            mIdTokenTextView.text = "null"


            signIn.visibility = View.VISIBLE
            signOut.visibility = View.GONE
            mRefreshButton.visibility = View.GONE
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GET_TOKEN) {
            // [START get_id_token]
            // This task is always completed immediately, there is no need to attach an
            // asynchronous listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            // [END get_id_token]
        }
    }
}
