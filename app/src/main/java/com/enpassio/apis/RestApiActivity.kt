package com.enpassio.apis


import android.content.Intent
import android.net.Uri
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
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


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
    lateinit var restApiPlayground: Button
    // Scope for reading user's contacts
    private val CONTACTS_SCOPE = "https://mail.google.com/"
    private val redirectUri = BuildConfig.REDIRECT_URI

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
        restApiPlayground = findViewById(R.id.button_rest_api_playground)
        restApiPlayground.setOnClickListener { playWithRestApi() }


        // [START configure_signin]
        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_API_CLIENT_ID)
                .requestEmail()
                .requestScopes(Scope(CONTACTS_SCOPE))
                .build()
        // [END configure_signin]

        // Build GoogleAPIClient with the Google Sign-In API and the above options.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


    }

    private fun playWithRestApi() {
        val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://accounts.google.com/o/oauth2/v2/auth?prompt=consent"
                        + "&response_type=code&client_id="
                        + BuildConfig.GOOGLE_API_CLIENT_ID
                        + "&scope=" + CONTACTS_SCOPE
                        + "&access_type=offline&Content-Type=application/json"
                        + "&redirect_uri=" + redirectUri))
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        val uri = intent.data
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            val code = uri.getQueryParameter("code")
            Log.v("my_tag", "uri received is: " + uri.toString())
            if (code != null) {
                // get access token
                Log.v("my_tag", "code is: " + code)
                // get access token
                val loginService = APIClient.client.create(LoginService::class.java)

                val call = loginService.getAccessToken(CONTACTS_SCOPE, code, BuildConfig.GOOGLE_API_CLIENT_ID, redirectUri, "authorization_code")

                call.enqueue(object : Callback<AccessToken> {
                    override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                        val accessToken = response.errorBody().toString()
                        Log.v("my_tag", "accessToken is: " + response.body()?.accessToken)
                        Log.v("my_tag", "response.errorBody() is: " + accessToken)
                        Log.v("my_tag", "response.message() is: " + response.message())
                        Log.v("my_tag", "response.code() is: " + response.code())
                        Log.v("my_tag", "response.headers() is: " + response.headers())
                        Log.v("my_tag", "response.raw() is: " + response.raw())
                    }

                    override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                        Log.e("my_tag", "error is: " + t.message)
                    }
                })
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }


    // [START handle_sign_in_result]
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
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

            fetchUsersData(account)
        } else {
            mIdTokenTextView.text = "null"


            signIn.visibility = View.VISIBLE
            signOut.visibility = View.GONE
            mRefreshButton.visibility = View.GONE
        }
    }

    private fun fetchUsersData(account: GoogleSignInAccount) {


        val credential = GoogleAccountCredential.usingOAuth2(
                this@RestApiActivity,
                Collections.singleton(CONTACTS_SCOPE))
        credential.selectedAccount = account.account


        val thread = Thread() {
            kotlin.run {
                val loginService = ServiceGenerator.createService(GmailService::class.java, credential.token)
                val call = loginService.getListOfEmails(account.email!!)
                call.enqueue(object : Callback<ListOfMailIds> {
                    override fun onResponse(call: Call<ListOfMailIds>, response: Response<ListOfMailIds>) {
                        val listOfIdsOfMails = response.body()
                        Log.v("my_tag", "data received is: " + listOfIdsOfMails)

                    }

                    override fun onFailure(call: Call<ListOfMailIds>, t: Throwable) {
                        Log.e("my_tag", "error is: " + t.message)
                    }
                })
            }
        }
        thread.start()
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
