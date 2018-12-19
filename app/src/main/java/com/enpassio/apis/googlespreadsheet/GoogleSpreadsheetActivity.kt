package com.enpassio.apis.googlespreadsheet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.enpassio.apis.AccessToken
import com.enpassio.apis.BuildConfig
import com.enpassio.apis.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleSpreadsheetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_spreadsheet)
    }


    private fun getSpreadsheetData(refreshToken: String) {
        val refreshTokenService = SpreadsheetClient.client.create(SpreadsheetService::class.java)

        //https://sheets.googleapis.com/v4/spreadsheets/10uRokjEVBWRdE7wHas_An5nXFU01tO_kuZzWjQbIJBI?ranges=A1:C10&fields=properties.title,sheets(sheetProperties,data.rowData.values(effectiveValue,effectiveFormat))&key=my-api-key
        val call =
                refreshTokenService.getSpreadsheetData(
                        "10uRokjEVBWRdE7wHas_An5nXFU01tO_kuZzWjQbIJBI",
                        "properties.title,sheets(sheetProperties,data.rowData.values(effectiveValue,effectiveFormat))",
                        BuildConfig.SPREADSHEET_API_KEY)
        call.enqueue(object : Callback<AccessToken> {
            override fun onFailure(call: Call<AccessToken>, t: Throwable) {}
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                Log.v("my_tag", "token from refresh token is :" + response.body()?.accessToken)
            }
        })
    }
}
