package com.example.intervaltimer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.example.intervaltimer.databinding.FragmentFirstBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val workoutCollectionDataStore: DataStore<WorkoutCollectionStore> by lazy { requireContext().workoutCollectionDataStore }

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

        lifecycleScope.launch {
            //val workoutTimeStr = workout.workoutTime.toString()
            val workoutFromCollection = workoutCollectionDataStore.data.first().workoutList
            val newText = "Workouts from Collection ${workoutFromCollection.size}\n$workoutFromCollection"
            binding.textviewFirst.text = newText
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}