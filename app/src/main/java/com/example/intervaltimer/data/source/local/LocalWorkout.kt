package com.example.intervaltimer.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout"
)
data class LocalWorkout(
    @PrimaryKey val id: String,
    val workoutTime: Int = 0,
    val breakTime: Int = 0,
    val numSets: Int = 0,
)