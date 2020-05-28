package com.example.clockit

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

class Notification {

    companion object {
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "Time App Timer"
        private const val TIMER_ID = 0


        fun TimerExpiredShow(context: Context) {
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.action = AppParameters.ACTION_START
            val pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder=getNotificationBuilder(context, CHANNEL_ID_TIMER,true)
            notificationBuilder.setContentTitle("Time Expired")
                .setContentText("Start Again?")
                .setContentIntent(getPendingIntentWithStack(context,MainActivity::class.java))
                .addAction(R.drawable.ic_play,"Start",pendingIntent)
            val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER,true)
            notificationManager.notify(TIMER_ID,notificationBuilder.build())

        }

        fun ShowWhileTimerRunning(context: Context,wakeUpTime:Long) {
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.action = AppParameters.ACTION_START
            val pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val dateformat=SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

            val notificationBuilder=getNotificationBuilder(context, CHANNEL_ID_TIMER,true)
            notificationBuilder.setContentTitle("Time Running")
                .setContentText("End?: ${dateformat.format(Date(wakeUpTime))}")
                .setContentIntent(getPendingIntentWithStack(context,MainActivity::class.java))
                .setOngoing(true)
                .addAction(R.drawable.ic_pause,"Pause",pendingIntent)
                .addAction(R.drawable.stop,"Stop",pendingIntent)
            val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER,true)
            notificationManager.notify(TIMER_ID,notificationBuilder.build())

        }

        fun showPausedTimer(context: Context) {
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.action = AppParameters.ACTION_START
            val pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder=getNotificationBuilder(context, CHANNEL_ID_TIMER,true)
            notificationBuilder.setContentTitle("Time is Paused")
                .setContentText("Resume?")
                .setContentIntent(getPendingIntentWithStack(context,MainActivity::class.java))
                .setOngoing(true)
                .addAction(R.drawable.ic_play,"Resume",pendingIntent)

            val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER,true)
            notificationManager.notify(TIMER_ID,notificationBuilder.build())

        }

        fun HideNotificationOfTime(context: Context){
            val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(TIMER_ID)
        }

        private fun getNotificationBuilder(context: Context, channelId: String, playsound: Boolean): NotificationCompat.Builder {
            val sound:Uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder=NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.drawable.ic_timer).setAutoCancel(true)
                .setDefaults(0)
            if (playsound) notificationBuilder.setSound(sound)
            return notificationBuilder
        }

        private fun <T> getPendingIntentWithStack(context: Context,javaClass:Class<T>):PendingIntent{
            val finalIntent=Intent(context,javaClass)
            finalIntent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val stackBuilder=TaskStackBuilder.create(context)
                stackBuilder.addParentStack(javaClass)
                stackBuilder.addNextIntent(finalIntent)
            return stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

        }
        @TargetApi(26)
        private fun NotificationManager.createNotificationChannel(channelId: String,
        channelName:String,playsound: Boolean){
            val channelImportance=if (playsound) NotificationManager.IMPORTANCE_DEFAULT
            else NotificationManager.IMPORTANCE_LOW
            val notificationChannel=NotificationChannel(channelId,channelName,channelImportance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor= Color.MAGENTA
            this.createNotificationChannel(notificationChannel)
        }
    }
}