package com.example.intervaltimer

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.intervaltimer.databinding.FragmentFirstBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val dataStore: DataStore<WorkoutStore> by lazy { requireContext().dataStore }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            // binding.textviewFirst.text = "Your new text here"
            lifecycleScope.launch {
                val workout = dataStore.data.first()
                val workoutTimeStr = workout.workoutTime.toString()
                val newText = "Workout object: $workout \nWorkout time: $workoutTimeStr"
                binding.textviewFirst.text = newText
            }
            // findNavController().navigate(R.id.action_FirstFragment_to_AddTimerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}