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

    val args: SecondFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val textView = binding.textviewTimer
        var workout: WorkoutStore

        lifecycleScope.launch {
            workout = workoutDataStore.data.first().workoutList[0]

            // set timer text field
            val workoutTimeStr = workout.workoutTime.toString()
            binding.textviewTimer.text = workoutTimeStr
            // set numSets
            numSetsRemaining = workout.numSets
            updateSetInfo()
            woTimer = createWoTimer(workout.workoutTime.toLong(), textView)     // set workout timer
            breakTimer = createBreakTimer(workout.breakTime.toLong(), textView)     // set break timer
        }

        return binding.root

    }

    private fun createBreakTimer(timeInSeconds: Long, textView: TextView): CountDownTimer {
        return object : CountDownTimer(timeInSeconds * 1000, 10) {

            override fun onTick(millisUntilFinished: Long) {
                textView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                updateWoInfoToWo()
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
                    updateWoInfoToBreak()
                    breakTimer.start()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val workout: WorkoutStore = workoutDataStore.data.first().workoutList[args.woIndex]
            val textView = binding.textviewTimer
            val workoutTimeStr = workout.workoutTime.toString()

            textView.text = workoutTimeStr
            woTimer = createWoTimer(workout.workoutTime.toLong(), textView)     // set workout timer
            breakTimer = createBreakTimer(workout.breakTime.toLong(), textView)     // set break timer
            numSetsRemaining = workout.numSets
            updateSetInfo()
        }

        binding.buttonSecond.setOnClickListener {
            updateWoInfoToWo()
            woTimer.start()
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

    private fun updateWoInfoToWo(){
        val newTextWo = "${getString(R.string.woInfoRoutine)} Workout"
        binding.tvWoInfo.text = newTextWo
        updateSetInfo()
    }

    private fun updateWoInfoToBreak(){
        val newTextWo = "${getString(R.string.woInfoRoutine)} Break"
        binding.tvWoInfo.text = newTextWo
    }

    private fun updateSetInfo(){
        val newTextSets = "$numSetsRemaining ${getString(R.string.remainingSetsInfo)}"
        binding.tvSetsInfo.text = newTextSets
    }
}