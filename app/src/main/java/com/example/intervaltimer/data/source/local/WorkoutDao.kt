package com.example.intervaltimer.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Query("SELECT * FROM workout")
    fun observeAll(): Flow<List<LocalWorkout>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(workout: LocalWorkout)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(workout: List<LocalWorkout>)

    // @Query("UPDATE workout SET isCompleted = :completed WHERE id = :workoutId")
    // suspend fun updateCompleted(workoutId: String, completed: Boolean)

    @Query("DELETE FROM workout")
    suspend fun deleteAll()

}





