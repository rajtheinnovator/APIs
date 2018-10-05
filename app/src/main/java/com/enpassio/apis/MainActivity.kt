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
}
