package com.example.intervaltimer

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.intervaltimer.databinding.FragmentSecondBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val workoutDataStore: DataStore<WorkoutCollectionStore> by lazy { requireContext().workoutDataStore }
    private lateinit var woTimer: CountDownTimer
    private lateinit var breakTimer: CountDownTimer
    private var numSetsRemaining: Int = 99

    private val args: SecondFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val workout: WorkoutStore = workoutDataStore.data.first().workoutList[args.woIndex]
            val textView = binding.textviewTimer
            val workoutTimeStr = workout.workoutTime.toString()

            textView.text = workoutTimeStr  // set timer text field
            numSetsRemaining = workout.numSets      // set num sets
            updateSetInfo()     // set num sets text field
            woTimer = createWoTimer(workout.workoutTime.toLong(), textView)     // set workout timer
            breakTimer = createBreakTimer(workout.breakTime.toLong(), textView)     // set break timer
        }

        binding.buttonSecond.setOnClickListener {
            updateRoutineInfoToWo()
            woTimer.start()
        }
    }

    private fun createBreakTimer(timeInSeconds: Long, textView: TextView): CountDownTimer {
        return object : CountDownTimer(timeInSeconds * 1000, 10) {

            override fun onTick(millisUntilFinished: Long) {
                textView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                updateRoutineInfoToWo()
                woTimer.start()
            }
        }
    }

    private fun createWoTimer(timeInSeconds: Long, textView: TextView): CountDownTimer {
        return object : CountDownTimer(timeInSeconds * 1000, 10) {

            override fun onTick(millisUntilFinished: Long) {
                textView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                numSetsRemaining--
                updateSetInfo()
                if (numSetsRemaining == 0) {
                    textView.text = getString(R.string.workoutFinishedText)
                } else {
                    updateRoutineInfoToBreak()
                    breakTimer.start()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        woTimer.cancel()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        woTimer.cancel()
    }

    private fun updateRoutineInfoToWo(){
        binding.tvWoInfo.text = getString(R.string.workout)
        updateSetInfo()
    }

    private fun updateRoutineInfoToBreak(){
        binding.tvWoInfo.text = getString(R.string.break_str)
    }

    private fun updateSetInfo(){
        val newTextSets = "$numSetsRemaining ${getString(R.string.remainingSetsInfo)}"
        binding.tvSetsInfo.text = newTextSets
    }
}