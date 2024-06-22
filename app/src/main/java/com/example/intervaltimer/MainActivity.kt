package com.example.intervaltimer

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.dataStore
import androidx.lifecycle.lifecycleScope
import com.example.intervaltimer.databinding.ActivityMainBinding
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.launch

val Context.workoutDataStore by dataStore("workout-collection-store.json", WorkoutCollectionStoreSerializer)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.fab.setOnClickListener {
            lifecycleScope.launch {
                navController.navigate(R.id.action_FirstFragment_to_AddTimerFragment)
            }
        }

        window.navigationBarColor = resources.getColor(R.color.dark_blue, theme)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    suspend fun createWorkoutInCollection(workoutTime: Int, breakTime: Int, numSets: Int) {
        workoutDataStore.updateData {
            it.copy(
                workoutList = it.workoutList.mutate { workoutList ->
                    workoutList.add(
                        WorkoutStore(
                            workoutTime = workoutTime,
                            breakTime = breakTime,
                            numSets = numSets,
                        )
                    )
                }
            )
        }
    }
}