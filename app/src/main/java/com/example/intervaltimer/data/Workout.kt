package com.example.intervaltimer.data

/**
 * Immutable model class for a workout.
 *
 * @param workoutTime length of round in seconds
 * @param breakTime length of break in seconds
 * @param numSets number of sets
 * @param id id of the workout TODO: Do I need this?
 */

data class Workout(
    val workoutTime: Int = 0,
    val breakTime: Int = 0,
    val numSets: Int = 0,
    val id: String,
) {
}

/**
 * Immutable model class for a Task.
 *
 * @param title title of the task
 * @param description description of the task
 * @param isCompleted whether or not this task is completed
 * @param id id of the task
 *
 * TODO: The constructor of this class should be `internal` but it is used in previews and tests
 *  so that's not possible until those previews/tests are refactored.
 */
/*
data class Task(
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val id: String,
) {

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description

    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}*/
