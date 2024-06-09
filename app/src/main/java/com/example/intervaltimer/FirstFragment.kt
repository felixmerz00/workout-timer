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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val workoutDataStore: DataStore<WorkoutCollectionStore> by lazy { requireContext().workoutDataStore }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            val dataset = workoutDataStore.data.first().workoutList
            val workoutAdapter = WorkoutAdapter(dataset)

            val recyclerView: RecyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = workoutAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            //val workoutTimeStr = workout.workoutTime.toString()
            val workoutFromCollection = workoutDataStore.data.first().workoutList
            val newText = "Workout Collection Size: ${workoutFromCollection.size}"
            binding.textviewFirst.text = newText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}