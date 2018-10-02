package com.enpassio.apis.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.enpassio.apis.RestApiActivity


class AlarmApiCallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // For our recurring task, we'll just display a message
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show()
        val apiCallIntent = Intent(context, RestApiActivity::class.java)
        val bundle = Bundle()
        bundle.putString("extra_api", "extra_api")
        apiCallIntent.putExtra("extra_bundle", bundle)
        //apiCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(apiCallIntent)
    }
}