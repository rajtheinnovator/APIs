package com.enpassio.apis.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.enpassio.apis.RestApiActivity

class AlarmToneReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        // For our recurring task, we'll just display a message
        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, RestApiActivity::class.java))
    }
}