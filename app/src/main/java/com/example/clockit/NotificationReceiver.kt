package com.example.clockit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.clockit.util.UtilPreference
import java.sql.Time

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
       when(intent.action){
           AppParameters.ACTION_STOP->{
               MainActivity.alarmRemove(context)
               UtilPreference.setTimerState(MainActivity.Timer.stopped,context)
               Notification.HideNotificationOfTime(context)
           }
           AppParameters.ACTION_PAUSE->{
             var   secondsRemaining=UtilPreference.getSecondsRemaining(context)
             val alarmTimeSet=UtilPreference.getAlarmAndTimeSet(context)
               val presentTime=MainActivity.presentTime
               secondsRemaining-=presentTime-alarmTimeSet
               UtilPreference.SetSecondsRemaining(secondsRemaining,context)
               Notification.showPausedTimer(context)
           }
           AppParameters.ACTION_RESUME->{
               val secondsRemaining=UtilPreference.getSecondsRemaining(context)
               val wakeUpTime=MainActivity.setAlarm(context,MainActivity.presentTime,secondsRemaining)
               UtilPreference.setTimerState(MainActivity.Timer.ticking,context)
               Notification.ShowWhileTimerRunning(context,wakeUpTime)
           }
           AppParameters.ACTION_START->{
               val minutesRemaining=UtilPreference.obtainTimerLength(context)
               val secondaRemaining=minutesRemaining*60L
               val wakeUpTime=MainActivity.setAlarm(context,MainActivity.presentTime,secondaRemaining)
               UtilPreference.setTimerState(MainActivity.Timer.ticking,context)
               UtilPreference.SetSecondsRemaining(secondaRemaining,context )
               Notification.ShowWhileTimerRunning(context,wakeUpTime)
           }
       }
    }
}
