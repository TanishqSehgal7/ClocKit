package com.example.clockit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.clockit.util.UtilPreference

class TimerExpired : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Notification.TimerExpiredShow(context)
        UtilPreference.setTimerState(MainActivity.Timer.stopped,context)
        UtilPreference.setAlarmTime(0,context)

    }
}
