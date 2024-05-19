package com.example.intervaltimer.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.intervaltimer.data.Workout

@Entity(
    tableName = "workout"
)
data class LocalWorkout(
    @PrimaryKey val id: String,
    val workoutTime: Int = 0,
    val breakTime: Int = 0,
    val numSets: Int = 0,
)


// Convert a LocalWorkout to a Task
fun LocalWorkout.toExternal() = Workout(
    id = id,
    workoutTime = workoutTime,
    breakTime = breakTime,
    numSets = numSets,
)

// Convenience function which converts a list of LocalTasks to a list of Tasks
fun List<LocalWorkout>.toExternal() = map(LocalWorkout::toExternal) // Equivalent to map { it.toExternal() }

fun Workout.toLocal() = LocalWorkout(
    id = id,
    workoutTime = workoutTime,
    breakTime = breakTime,
    numSets = numSets,
)
