package com.example.intervaltimer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class TimerService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> startTimer()
            Actions.STOP.toString() -> stopSelf()
            Actions.RESET.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer() {
        val notification = NotificationCompat.Builder(this, "workout_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Workout Timer")
            .setContentText("Workout 00:42")
            .build()
        startForeground(1, notification)
    }

    enum class Actions {
        START, STOP, RESET
    }

}