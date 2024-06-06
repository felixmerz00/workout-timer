package com.example.intervaltimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.intervaltimer.databinding.FragmentAddTimerBinding
import kotlinx.coroutines.launch

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class AddTimer : Fragment() {

    private var _binding: FragmentAddTimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dataStore: DataStore<WorkoutStore> by lazy { requireContext().dataStore }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddTimerBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonConfirm.setOnClickListener {
            lifecycleScope.launch {
                val woTime = convertTimeStrToInt(binding.etWoDuration.text.toString())
                val bTime = convertTimeStrToInt(binding.etBreakDuration.text.toString())
                val nSets = binding.etNumSets.text.toString().toInt()
                (requireActivity() as? MainActivity)?.createWorkout(woTime, bTime, nSets)

                findNavController().navigate(R.id.action_AddTimerFragment_to_FirstFragment)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun convertTimeStrToInt(durationStr: String): Int {
        val parts = durationStr.split(":")
        val minutes = parts[0].toIntOrNull()
        val seconds = parts[1].toIntOrNull()
        if (minutes == null || seconds == null) return 999
        return (minutes * 60) + seconds
    }
}