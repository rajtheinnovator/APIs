package com.enpassio.apis

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.enpassio.apis.alarm.AlarmApiCallReceiver
import com.enpassio.apis.alarm.AlarmToneReceiver
import com.enpassio.apis.googlespreadsheet.GoogleSpreadsheetActivity
import com.enpassio.apis.mapsexample.MapsExampleActivity


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    var alarmTonePendingIntent: PendingIntent? = null
    var alarmApiCallPendingIntent: PendingIntent? = null
    var manager: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        val restApiButton: Button = findViewById(R.id.rest_api)
        restApiButton.setOnClickListener { startRESTApiActivity() }

        val paymentGatwayButton: Button = findViewById(R.id.payment_gateway)
        paymentGatwayButton.setOnClickListener { startPaymentGatewayActivity() }

        //handle alarm and braodcast
        // Retrieve a PendingIntent that will perform a broadcast
        val alarmToneIntent = Intent(this, AlarmToneReceiver::class.java)
        alarmTonePendingIntent = PendingIntent.getBroadcast(this, 0, alarmToneIntent, 0)

        val alarmApiCallIntent = Intent(this, AlarmApiCallReceiver::class.java)
        alarmApiCallPendingIntent = PendingIntent.getBroadcast(this, 0, alarmApiCallIntent, 0)
        //set the alarm broadcast
        //startAlarmToRingAlarmToneForNotification()
        startAlarmToFetchDataFromApi()

        //handle click on webView
        val webViewForButtonClick = findViewById<Button>(R.id.web_view_click);
        webViewForButtonClick.setOnClickListener { startWebViewClickActivity() }

        //handle Google Spreadsheet
        val googleSpreadsheetButton: Button = findViewById(R.id.spreadsheet_example)
        googleSpreadsheetButton.setOnClickListener { startGoogleSpreadsheetActivity() }

        //handle maps activity
        val mapsExampleActivityButton: Button = findViewById(R.id.maps_example)
        mapsExampleActivityButton.setOnClickListener { startMapsExampleActivity() }
    }

    private fun startMapsExampleActivity() {
        startActivity(Intent(this@MainActivity, MapsExampleActivity::class.java))
    }

    private fun startGoogleSpreadsheetActivity() {
        startActivity(Intent(this@MainActivity, GoogleSpreadsheetActivity::class.java))
    }

    private fun startWebViewClickActivity() {
        startActivity(Intent(this@MainActivity, WebViewClickActivity::class.java))
    }

    private fun startPaymentGatewayActivity() {
        startActivity(Intent(this, PaymentGatewayActivity::class.java))
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
}
