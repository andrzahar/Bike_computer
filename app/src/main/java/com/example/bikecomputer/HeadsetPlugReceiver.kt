package com.example.bikecomputer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class HeadsetPlugReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ttt", "onReceive")
        if (intent.action.equals(Intent.ACTION_HEADSET_PLUG)) {
            val state = intent.getIntExtra("state", -1)
            when (state) {
                0 -> {}
                1 -> {
                    val startIntent = context
                        .packageManager
                        .getLaunchIntentForPackage(context.packageName)

                    startIntent!!.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    context.startActivity(startIntent)
                }
            }
        }
    }
}