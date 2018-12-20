package com.enpassio.apis.googlespreadsheet

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.enpassio.apis.BuildConfig
import com.enpassio.apis.R
import com.enpassio.apis.ServiceGenerator
import com.enpassio.apis.googlespreadsheet.model.ListSpreadsheet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleSpreadsheetActivity : AppCompatActivity() {

    private val CONTACTS_SCOPE = "https://mail.google.com/+https://www.googleapis.com/auth/userinfo.email"
    private val DRIVE_SCOPE = "https://www.googleapis.com/auth/drive.readonly"
    private val SPREADSHEET_SCOPE = "https://www.googleapis.com/auth/spreadsheets.readonly"
    private val redirectUri = BuildConfig.REDIRECT_URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_spreadsheet)
        val settings = getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = settings.getString("token", "")!!
        getListOfMail(token)
    }


    private fun getListOfMail(token: String) {
        val tokenForUser = token
        val mailListService = ServiceGenerator.createService(ListSpreadsheetService::class.java, tokenForUser)
        val mailCall = mailListService.getSpreadsheetList("application/vnd.google-apps.spreadsheet", CONTACTS_SCOPE + DRIVE_SCOPE + SPREADSHEET_SCOPE,
                BuildConfig.GOOGLE_API_CLIENT_ID,
                redirectUri)
        mailCall.enqueue(object : Callback<ListSpreadsheet> {
            override fun onResponse(call: Call<ListSpreadsheet>, response: Response<ListSpreadsheet>) {
                val listOfSpreadsheet = response.body()

                Log.v("my_taggggg", "token inside spreadsheet data received is: " + tokenForUser)
                Log.v("my_taggggg", "listOfSpreadsheet data received is: " + listOfSpreadsheet)
                Log.v("my_taggggg", "response.errorBody() is: " + listOfSpreadsheet)
                Log.v("my_taggggg", "response.message() is: " + response.message())
                Log.v("my_taggggg", "response.code() is: " + response.code())
                Log.v("my_taggggg", "response.headers() is: " + response.headers())
                Log.v("my_taggggg", "response.raw() is: " + response.raw())
                for (fie in listOfSpreadsheet?.files!!) {
                    Log.v("my_taggggg", "spreadsheet id is: " + fie.id)
                }

            }

            override fun onFailure(call: Call<ListSpreadsheet>, t: Throwable) {
                Log.e("my_taggggg", "error is: " + t.message)
            }
        })
    }
}
