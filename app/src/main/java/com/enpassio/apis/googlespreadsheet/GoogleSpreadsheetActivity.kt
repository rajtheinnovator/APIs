package com.enpassio.apis.googlespreadsheet

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.enpassio.apis.BuildConfig
import com.enpassio.apis.R
import com.enpassio.apis.ServiceGenerator
import com.enpassio.apis.googlespreadsheet.model.ListSpreadsheet
import com.enpassio.apis.googlespreadsheet.model.ValueRange
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleSpreadsheetActivity : AppCompatActivity() {

    private val DRIVE_SCOPE = "https://www.googleapis.com/auth/drive.readonly   https://www.googleapis.com/auth/spreadsheets.readonly"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_spreadsheet)
        val settings = getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = settings.getString("token", "")!!
        getListOfSpreadsheets(token)
    }


    private fun getListOfSpreadsheets(token: String) {
        val tokenForUser = token
        val listSpreadsheetService = ServiceGenerator.createService(ListSpreadsheetService::class.java, tokenForUser)
        val listSpreadsheetCall = listSpreadsheetService.getSpreadsheetList("application/vnd.google-apps.spreadsheet", DRIVE_SCOPE,
                BuildConfig.GOOGLE_API_CLIENT_ID)
        listSpreadsheetCall.enqueue(object : Callback<ListSpreadsheet> {
            override fun onResponse(call: Call<ListSpreadsheet>, response: Response<ListSpreadsheet>) {
                val listOfSpreadsheet = response.body()

                Log.v("my_taggggg", "token inside spreadsheet data received is: " + tokenForUser)
                Log.v("my_taggggg", "listOfSpreadsheet data received is: " + listOfSpreadsheet)
                Log.v("my_taggggg", "response.errorBody() is: " + listOfSpreadsheet)
                Log.v("my_taggggg", "response.message() is: " + response.message())
                Log.v("my_taggggg", "response.code() is: " + response.code())
                Log.v("my_taggggg", "response.headers() is: " + response.headers())
                Log.v("my_taggggg", "response.raw() is: " + response.raw())
                for (file in listOfSpreadsheet?.files!!) {
                    Log.v("my_taggggg", "spreadsheet id is: " + file.id)
//                    callForSingleSpreadsheetData(file.id, token)
                }
                callForSingleSpreadsheetData(listOfSpreadsheet.files!!.get(0).id, token)
            }

            override fun onFailure(call: Call<ListSpreadsheet>, t: Throwable) {
                Log.e("my_taggggg", "error is: " + t.message)
            }
        })
    }

    private fun callForSingleSpreadsheetData(id: String?, token: String) {
        val spreadsheetService = SpreadsheetServiceGenerator.createService(SpreadsheetService::class.java, token)
        val spreadsheetCall = spreadsheetService.getSpreadsheetData("10uRokjEVBWRdE7wHas_An5nXFU01tO_kuZzWjQbIJBI", "A1:Z")
        spreadsheetCall.enqueue(object : Callback<ValueRange> {
            override fun onFailure(call: Call<ValueRange>, t: Throwable) {
                Log.e("my_taggsss", "single error is: " + t.message)
            }

            override fun onResponse(call: Call<ValueRange>, response: Response<ValueRange>) {
                val valueRange = response.body()
                if (valueRange?.values != null) {
                    for (singleValue in valueRange.values!!) {
                        if (singleValue.size > 0) {
                            Log.v("my_taggsss", "Spreadsheet data value is: " + singleValue)

                        }
                    }
                }
            }
        })
    }
}
