package com.enpassio.apis.googlespreadsheet

import com.enpassio.apis.ListOfMailIds
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Abhishek Raj on 12/19/2018.
 */
//https://sheets.googleapis.com/v4/spreadsheets/10uRokjEVBWRdE7wHas_An5nXFU01tO_kuZzWjQbIJBI?ranges=A1:C10&fields=properties.title,sheets(sheetProperties,data.rowData.values(effectiveValue,effectiveFormat))&key=AIzaSyBsh0FQJWnBPvRgZJUSKrCNAjH99q2OAH0
interface SpreadsheetService {
    @GET("v4/spreadsheets/{spreadsheetId}?")
    fun getSpreadsheetData(@Path("spreadsheetId") spreadsheetId: String,
                           @Query("ranges") ranges: String,
                           @Query("key") key: String): Call<ListOfMailIds>
}