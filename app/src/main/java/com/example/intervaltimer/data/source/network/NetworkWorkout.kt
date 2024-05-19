package com.example.intervaltimer.data.source.network

import com.example.intervaltimer.data.source.local.LocalWorkout

data class NetworkWorkout (
    val id: String,
    val workoutTime: Int = 0,
    val breakTime: Int = 0,
    val numSets: Int = 0,
    val priority: Int? = null,
    val status: WorkoutStatus = WorkoutStatus.ACTIVE
) {
    enum class WorkoutStatus {
        ACTIVE,
        COMPLETE
    }
}

fun NetworkWorkout.toLocal() = LocalWorkout(
    id = id,
    workoutTime = workoutTime,
    breakTime = breakTime,
    numSets = numSets,
    // isCompleted = (status == NetworkWorkout.WorkoutStatus.COMPLETE),
)

fun List<NetworkWorkout>.toLocal() = map(NetworkWorkout::toLocal)

fun LocalWorkout.toNetwork() = NetworkWorkout(
    id = id,
    workoutTime = workoutTime,
    breakTime = breakTime,
    numSets = numSets,
    // status = if (isCompleted) { NetworkWorkout.WorkoutStatus.COMPLETE } else { NetworkWorkout.WorkoutStatus.ACTIVE }
)

fun List<LocalWorkout>.toNetwork() = map(LocalWorkout::toNetwork)