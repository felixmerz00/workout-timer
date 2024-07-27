package com.example.intervaltimer

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat

class TimerService: Service() {
    private lateinit var warmUpTimer: CountDownTimer
    private lateinit var woTimer: CountDownTimer
    private lateinit var breakTimer: CountDownTimer
    private var currentSet = 0
    private var totalSets = 0
    private val breakSounds = listOf(R.raw.starting_workout_up_next_set_one, R.raw.break_up_next_set_two, R.raw.break_up_next_set_three, R.raw.break_up_next_set_four, R.raw.break_up_next_set_five, R.raw.break_up_next_set_six, R.raw.break_up_next_set_seven, R.raw.break_up_next_set_eight, R.raw.break_up_next_set_nine, R.raw.break_up_next_set_ten)
    private val woSounds = listOf(R.raw.lets_go_set_one, R.raw.lets_go_set_two, R.raw.lets_go_set_three, R.raw.lets_go_set_four, R.raw.lets_go_set_five, R.raw.lets_go_set_six, R.raw.lets_go_set_seven, R.raw.lets_go_set_eight, R.raw.lets_go_set_nine, R.raw.lets_go_set_ten)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        when(intent?.action) {
//            Actions.START.toString() -> startTimer()
//            Actions.STOP.toString() -> stopSelf()
//            Actions.RESET.toString() -> stopSelf()
//        }

        val workoutTime = intent?.getLongExtra("workoutTime", 60L) ?: 60L
        val breakTime = intent?.getLongExtra("breakTime", 30L) ?: 30L
        totalSets = intent?.getIntExtra("totalSets", 3) ?: 3

        startForeground(NOTIFICATION_ID, createNotification("Workout Timer Running"))

        startWarmUpTimer(workoutTime, breakTime)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startWarmUpTimer(workoutTime: Long, breakTime: Long) {
        var mediaPlayer = MediaPlayer.create(applicationContext, breakSounds[0])
        mediaPlayer.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer.start()

        warmUpTimer = object : CountDownTimer(10*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateNotification("Warm Up: ${millisUntilFinished / 1000} seconds left")
            }

            override fun onFinish() {
                currentSet++
                startWorkoutTimer(workoutTime, breakTime)
            }
        }.start()
    }

    private fun startWorkoutTimer(workoutTime: Long, breakTime: Long) {
        var mediaPlayer = MediaPlayer.create(applicationContext, woSounds[currentSet-1])
        mediaPlayer.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer.start()

        woTimer = object : CountDownTimer(workoutTime*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateNotification("Workout: ${millisUntilFinished / 1000} seconds left")
            }

            override fun onFinish() {
                if (currentSet < totalSets) {
                    startBreakTimer(breakTime, workoutTime)
                } else {
                    stopSelf()
                }
            }
        }.start()
    }

    private fun startBreakTimer(breakTime: Long, workoutTime: Long) {
        var mediaPlayer = MediaPlayer.create(applicationContext, breakSounds[currentSet])
        mediaPlayer.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer.start()

        breakTimer = object : CountDownTimer(breakTime*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateNotification("Break: ${millisUntilFinished / 1000} seconds left")
            }

            override fun onFinish() {
                currentSet++
                startWorkoutTimer(workoutTime, breakTime)
            }
        }.start()
    }

    private fun createNotification(content: String): Notification {

        /* What does setContentIntent()? It defines which activity to open when the use clicks the notification.
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
         */

        return NotificationCompat.Builder(this, "workout_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Workout Timer")
            .setContentText(content)
            // .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification(content: String) {
        val notification = createNotification(content)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        warmUpTimer.cancel()
        woTimer.cancel()
        breakTimer.cancel()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }

    enum class Actions {
        START, STOP, RESET
    }

}