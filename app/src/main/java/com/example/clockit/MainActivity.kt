package com.example.clockit

import android.content.IntentSender
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.example.clockit.util.UtilPreference

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.sql.Time

class MainActivity : AppCompatActivity() {

    enum class Timer{
        stopped,ticking,paused
    }
    private lateinit var timer: CountDownTimer
    private  var TimeLenInSecs:Long=0
    private var stateoftime=Timer.stopped
    private var remainingTime:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title="  ClockKit"

        tvtime.bringToFront()

        fab_play.setOnClickListener {v->
            startTime()
            stateoftime=Timer.ticking
            updateThebtns()
        }

        fab_pause.setOnClickListener { v->
            timer.cancel()
            stateoftime=Timer.paused
            updateThebtns()
        }

        fab_stop.setOnClickListener { v->
            timer.cancel()
            stateoftime=Timer.stopped
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()
        TimerInThis()
    }

    override fun onPause() {
        super.onPause()
        if (stateoftime==Timer.ticking){
            timer.cancel()
        }
        else if (stateoftime==Timer.paused){
            // make the timer run the background and show a notification
        }
        UtilPreference.AssignPreviousLengthSeconds(TimeLenInSecs,this)
        UtilPreference.SetSecondsRemaining(remainingTime,this)
        UtilPreference.setTimerState(stateoftime,this)
    }

    private  fun TimerInThis(){
        stateoftime=UtilPreference.getTimerState(this)
        if(stateoftime==Timer.stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()
        remainingTime= if (stateoftime==Timer.ticking || stateoftime==Timer.paused)
            UtilPreference.getSecondsRemaining(this)
        else
            TimeLenInSecs
        if (stateoftime==Timer.ticking)
            startTime()
        updateThebtns()
        updateCountDownUI()
            }

    private fun onTimerFinished(){
        stateoftime=Timer.stopped

        setNewTimerLength()
        waveLoadingView.progressValue=0

        UtilPreference.SetSecondsRemaining(TimeLenInSecs,this)
        remainingTime=TimeLenInSecs
        updateThebtns()
        updateCountDownUI()
    }

    private fun startTime(){
       stateoftime=Timer.ticking
        timer= object : CountDownTimer(remainingTime*1000,1000) {
            override fun onFinish() {

            }

            override fun onTick(milisUntilFinished: Long) {
                remainingTime=milisUntilFinished/1000
                updateCountDownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val LenInMins=UtilPreference.obtainTimerLength(this)
        TimeLenInSecs=(LenInMins*60L)
        waveLoadingView.progressValue=TimeLenInSecs.toInt()
    }

    private fun setPreviousTimerLength(){
        TimeLenInSecs=UtilPreference.PreviousTimeLenInSecs(this)
        waveLoadingView.progressValue=TimeLenInSecs.toInt()
    }

    private fun updateCountDownUI(){
        val minutessUntilFinished=remainingTime/60
        val secondsInMinuteUntilFinished=remainingTime-minutessUntilFinished*60
        val secondsStr=secondsInMinuteUntilFinished.toString()
        tvtime.text="$minutessUntilFinished:${
        if (secondsStr.length==2) secondsStr
        else "0"+secondsStr}"
        waveLoadingView.progressValue=60-(TimeLenInSecs-remainingTime).toInt()
    }

    private fun updateThebtns(){
        when(stateoftime){
            Timer.ticking->{
                waveLoadingView.startAnimation()
                fab_play.isEnabled=false
                fab_pause.isEnabled=true
                fab_stop.isEnabled=true
            }
            Timer.stopped->{
                waveLoadingView.pauseAnimation()
                fab_play.isEnabled=true
                fab_pause.isEnabled=false
                fab_stop.isEnabled=false
            }
            Timer.paused->{
                waveLoadingView.pauseAnimation()
                fab_play.isEnabled=true
                fab_pause.isEnabled=false
                fab_stop.isEnabled=true
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}


