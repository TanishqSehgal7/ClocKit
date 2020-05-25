package com.example.clockit.util

import android.content.Context
import android.preference.PreferenceManager
import com.example.clockit.MainActivity

class UtilPreference {
    companion object{
        fun obtainTimerLength(context: Context):Int {
            return 1
        }

        private const val PREVIOUS_TIME_LEN_ID="com.example.clockit.previous_timer_length"

        fun PreviousTimeLenInSecs(context: Context):Long{
            val preference=PreferenceManager.getDefaultSharedPreferences(context)
            return preference.getLong(PREVIOUS_TIME_LEN_ID,0)
        }

        fun AssignPreviousLengthSeconds(seconds:Long,context: Context){
            val editor=PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIME_LEN_ID,seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID="com.example.clockit.timer_state"

        fun getTimerState(context: Context):MainActivity.Timer{
            val preference=PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal=preference.getInt(TIMER_STATE_ID,0)
            return MainActivity.Timer.values()[ordinal]
        }

        fun setTimerState(state:MainActivity.Timer,context: Context){
            val editor=PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal=state.ordinal
            editor.putInt(TIMER_STATE_ID,ordinal)
            editor.apply()
        }

        private const val SECONDS_REMAINING_ID="com.example.clockit.seconds_remaining"

        fun getSecondsRemaining(context: Context):Long{
            val preference=PreferenceManager.getDefaultSharedPreferences(context)
            return preference.getLong(SECONDS_REMAINING_ID,0)
        }

        fun SetSecondsRemaining(seconds:Long,context: Context){
            val editor=PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID,seconds)
            editor.apply()
        }


    }
}