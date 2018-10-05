package com.enpassio.apis


import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import saschpe.android.customtabs.CustomTabsHelper
import saschpe.android.customtabs.WebViewFallback

class RestApiActivity : AppCompatActivity() {


    private val TAG = RestApiActivity::class.java.simpleName
    lateinit var mIdTokenTextView: TextView
    lateinit var restApiPlayground: Button
    // Scope for reading user's contacts
    private val CONTACTS_SCOPE = "https://mail.google.com/+https://www.googleapis.com/auth/userinfo.email"
    private val redirectUri = BuildConfig.REDIRECT_URI
    var player: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_api)
        mIdTokenTextView = findViewById(R.id.text_view_value_token)
        restApiPlayground = findViewById(R.id.button_rest_api_playground)
        restApiPlayground.setOnClickListener { checkIfTokenIsAvailable() }
        val alarmButton: Button = findViewById(R.id.button_ring_alarm)
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        player = MediaPlayer.create(this@RestApiActivity, notification)
        alarmButton.setOnClickListener {
            ringAlarm()
        }
    }

    //check if token exist already or not and act accordingly
    private fun checkIfTokenIsAvailable() {
        val settings = getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = settings.getString("token", "")!!
        if (token.isEmpty())
            setupTokenWithRestApi()
        else
            getAccessTokenFromRefreshToken(token)
        Log.v("my_tag", "token inside restApiActivity is: " + token)
    }

    private fun ringAlarm() {
        if (player?.isPlaying!!) {
            player?.stop()
        } else {
            val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            player = MediaPlayer.create(this@RestApiActivity, notification)
            player?.start()
        }
    }

    private fun setupTokenWithRestApi() {
        val icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.back);
        val customTabsIntent = CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(this.getResources()
                        .getColor(R.color.colorPrimary))
                .setShowTitle(true)
                .setCloseButtonIcon(icon)
                .build()
        // This is optional but recommended
        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);
        // This is where the magic happens...
        CustomTabsHelper.openCustomTab(this, customTabsIntent,
                Uri.parse("https://accounts.google.com/o/oauth2/v2/auth?prompt=consent"
                        + "&response_type=code&client_id="
                        + BuildConfig.GOOGLE_API_CLIENT_ID
                        + "&scope=" + CONTACTS_SCOPE
                        + "&access_type=offline&Content-Type=application/json"
                        + "&redirect_uri=" + redirectUri),
                WebViewFallback());
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
                /*
                Log.v("my_tag", "code is: " + code)
                */
                // get access token
                val loginService = APIClient.client.create(TokenService::class.java)

                val call =
                        loginService.getAccessTokenFromAuthCode(CONTACTS_SCOPE,
                                code,
                                BuildConfig.GOOGLE_API_CLIENT_ID,
                                redirectUri,
                                "authorization_code")

                call.enqueue(object : Callback<AccessToken> {
                    override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                        saveTokenToSharedPreference(response)
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


    private fun saveTokenToSharedPreference(accessTokenResponse: Response<AccessToken>) {
        // save token
        val settings: SharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        val editor: SharedPreferences.Editor = settings.edit();
        editor.putString("token", accessTokenResponse.body()?.accessToken);
        editor.commit()
        mIdTokenTextView.text = accessTokenResponse.body()?.accessToken
    }


    private fun getAccessTokenFromRefreshToken(refreshToken: String) {
        val refreshTokenService = APIClient.client.create(RefreshTokenService::class.java)
        val call =
                refreshTokenService.getAccessTokenFromRfreshToken(
                        refreshToken,
                        BuildConfig.GOOGLE_API_CLIENT_ID,
                        "refresh_token")
        call.enqueue(object : Callback<AccessToken> {
            override fun onFailure(call: Call<AccessToken>, t: Throwable) {}
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                Log.v("my_tag", "token from refresh token is :" + response.body()?.accessToken)
            }
        })
    }
}
