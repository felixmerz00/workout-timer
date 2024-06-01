package com.example.intervaltimer

import kotlinx.serialization.Serializable

/**
 * Immutable model class for a workout.
 *
 * @param workoutTime length of round in seconds
 * @param breakTime length of break in seconds
 * @param numSets number of sets
 */
@Serializable
data class WorkoutStore(
    val workoutTime: Int = 4,
    val breakTime: Int = 2,
    val numSets: Int = 3,
)
