package com.example.intervaltimer.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room Database that contains the Workout table.
 *
 * Note that exportSchema should be true in production databases.
 */

@Database(entities = [LocalWorkout::class], version = 1, exportSchema = false)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}
