package com.enpassio.apis

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.enpassio.apis.alarm.AlarmApiCallReceiver
import com.enpassio.apis.alarm.AlarmToneReceiver
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 97

    private var mGoogleSignInClient: GoogleSignInClient? = null

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    lateinit var userNameTextView: TextView
    lateinit var signInButton: Button
    lateinit var signOutButton: Button

    var alarmTonePendingIntent: PendingIntent? = null
    var alarmApiCallPendingIntent: PendingIntent? = null
    var manager: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        userNameTextView = findViewById(R.id.text_view_user_name)
        signInButton = findViewById(R.id.button_sign_in)
        signInButton.setOnClickListener { startSignProcess() }
        signOutButton = findViewById(R.id.button_sign_out)
        signOutButton.setOnClickListener { signOut() }

        val restApiButton: Button = findViewById(R.id.rest_api)
        restApiButton.setOnClickListener { startRESTApiActivity() }

        //handle alarm and braodcast
        // Retrieve a PendingIntent that will perform a broadcast
        val alarmToneIntent = Intent(this, AlarmToneReceiver::class.java)
        alarmTonePendingIntent = PendingIntent.getBroadcast(this, 0, alarmToneIntent, 0)

        val alarmApiCallIntent = Intent(this, AlarmApiCallReceiver::class.java)
        alarmApiCallPendingIntent = PendingIntent.getBroadcast(this, 0, alarmApiCallIntent, 0)
        //set the alarm broadcast
        //startAlarmToRingAlarmToneForNotification()
        startAlarmToFetchDataFromApi()
    }

    private fun startAlarmToFetchDataFromApi() {
        manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager?.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000 * 60, alarmApiCallPendingIntent);
    }

    //set alarm
    fun startAlarmToRingAlarmToneForNotification() {
        manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager?.set(AlarmManager.RTC, System.currentTimeMillis(), alarmTonePendingIntent);
    }

    private fun startRESTApiActivity() {
        startActivity(Intent(this, RestApiActivity::class.java))
    }

    private fun startSignProcess() {
        val signInIntent = mGoogleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode())
            updateUI(null)
        }

    }

    public override fun onStart() {
        super.onStart()

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
        // [END on_start_sign_in]
    }

    // [START signOut]
    private fun signOut() {
        mGoogleSignInClient?.signOut()
                ?.addOnCompleteListener(this) {
                    // [START_EXCLUDE]
                    updateUI(null)
                    // [END_EXCLUDE]
                }
    }

    // [END signOut]
    private fun updateUI(account: GoogleSignInAccount? = null) {
        if (account != null) {
            userNameTextView.text = account.displayName
            signInButton.visibility = View.GONE
            signOutButton.visibility = View.VISIBLE
        } else {
            userNameTextView.text = "Please sign in first"
            signInButton.visibility = View.VISIBLE
            signOutButton.visibility = View.GONE
        }
    }
}
