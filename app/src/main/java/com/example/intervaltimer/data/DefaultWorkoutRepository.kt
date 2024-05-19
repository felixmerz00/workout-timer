package com.example.intervaltimer.data

import com.example.intervaltimer.data.source.local.WorkoutDao
import com.example.intervaltimer.data.source.local.toExternal
import com.example.intervaltimer.data.source.local.toLocal
import com.example.intervaltimer.data.source.network.WorkoutNetworkDataSource
import com.example.intervaltimer.data.source.network.toLocal
import com.example.intervaltimer.data.source.network.toNetwork
import com.example.intervaltimer.di.ApplicationScope
import com.example.intervaltimer.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class DefaultWorkoutRepository @Inject constructor(
    private val localDataSource: WorkoutDao,
    private val networkDataSource: WorkoutNetworkDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) {
    fun observeAll() : Flow<List<Workout>> {
        return localDataSource.observeAll().map { workouts -> workouts.toExternal() }
    }

    // This method might be computationally expensive
    private fun createWorkoutId() : String {
        return UUID.randomUUID().toString()
    }

    suspend fun create(workoutTime: Int, breakTime: Int, numSets: Int): String {
        val workoutId = withContext(dispatcher) {
            createWorkoutId()
        }
        val workout = Workout(
            workoutTime = workoutTime,
            breakTime = breakTime,
            numSets = numSets,
            id = workoutId,
        )
        localDataSource.upsert(workout.toLocal())
        saveWorkoutsToNetwork()
        return workoutId
    }

//    This function for tasks does not apply to workouts.
//    suspend fun complete(workoutId: String) {
//        localDataSource.updateCompleted(workoutId, true)
//    }

    suspend fun refresh() {
        val networkWorkouts = networkDataSource.loadWorkouts()
        localDataSource.deleteAll()
        val localWorkouts = withContext(dispatcher) {
            networkWorkouts.toLocal()
        }
        localDataSource.upsertAll(networkWorkouts.toLocal())
    }

    private suspend fun saveWorkoutsToNetwork() {
        scope.launch {
            val localWorkouts = localDataSource.observeAll().first()
            val networkWorkouts = withContext(dispatcher) {
                localWorkouts.toNetwork()
            }
            networkDataSource.saveWorkouts(networkWorkouts)
        }
    }

}