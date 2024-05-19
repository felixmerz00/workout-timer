package com.example.intervaltimer.data.source.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class WorkoutNetworkDataSource @Inject constructor(){

    // A mutex is used to ensure that reads and writes are thread-safe.
    private val accessMutex = Mutex()
    private var tasks = listOf(
        NetworkWorkout(
            id = "PLANK",
            workoutTime = 60,
            breakTime = 30,
            numSets = 3,
        ),
        NetworkWorkout(
            id = "HIT",
            workoutTime = 30,
            breakTime = 30,
            numSets = 10,
        )
    )

    suspend fun loadWorkouts(): List<NetworkWorkout> = accessMutex.withLock {
        delay(SERVICE_LATENCY_IN_MILLIS)
        return tasks
    }

    suspend fun saveWorkouts(newTasks: List<NetworkWorkout>) = accessMutex.withLock {
        delay(SERVICE_LATENCY_IN_MILLIS)
        tasks = newTasks
    }
}

private const val SERVICE_LATENCY_IN_MILLIS = 2000L