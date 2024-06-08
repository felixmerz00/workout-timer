package com.example.intervaltimer

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutCollectionStore(

    @Serializable(with = MyPersistentListSerializer::class)
    val workoutList : PersistentList<WorkoutStore> = persistentListOf(
        WorkoutStore(4,3,2),
        WorkoutStore(5,4,3),
        WorkoutStore(6,5,4)
    ).toPersistentList()
)

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
