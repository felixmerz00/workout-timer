package com.example.intervaltimer

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.datastore.dataStore
import androidx.lifecycle.lifecycleScope
import com.example.intervaltimer.databinding.ActivityMainBinding
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.launch

val Context.workoutDataStore by dataStore("workout-collection-store.json", WorkoutCollectionStoreSerializer)

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Create a new workout", Snackbar.LENGTH_LONG).show()
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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