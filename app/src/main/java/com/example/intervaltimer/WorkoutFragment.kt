package com.example.intervaltimer

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.intervaltimer.databinding.FragmentWorkoutBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val workoutDataStore: DataStore<WorkoutCollectionStore> by lazy { requireContext().workoutDataStore }
    private lateinit var warmUpTimer: CountDownTimer
    private lateinit var woTimer: CountDownTimer
    private lateinit var breakTimer: CountDownTimer
    private var numSets: Int = 99
    private var numSetsRemaining: Int = 99
    private val breakSounds = listOf(R.raw.starting_workout_up_next_set_one, R.raw.break_up_next_set_two, R.raw.break_up_next_set_three, R.raw.break_up_next_set_four, R.raw.break_up_next_set_five, R.raw.break_up_next_set_six, R.raw.break_up_next_set_seven, R.raw.break_up_next_set_eight, R.raw.break_up_next_set_nine, R.raw.break_up_next_set_ten)
    private val woSounds = listOf(R.raw.lets_go_set_one, R.raw.lets_go_set_two, R.raw.lets_go_set_three, R.raw.lets_go_set_four, R.raw.lets_go_set_five, R.raw.lets_go_set_six, R.raw.lets_go_set_seven, R.raw.lets_go_set_eight, R.raw.lets_go_set_nine, R.raw.lets_go_set_ten)

    private val args: WorkoutFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val workout: WorkoutStore = workoutDataStore.data.first().workoutList[args.woIndex]
            val textView = binding.textviewTimer

            numSets = workout.numSets   // set num sets
            numSetsRemaining = workout.numSets      // set num sets
            updateSetInfo()     // set num sets text field
            warmUpTimer = createWarmUpTimer(textView)   // set warm-up timer for 10 sec
            woTimer = createWoTimer(workout.workoutTime.toLong(), textView)     // set workout timer
            breakTimer =
                createBreakTimer(workout.breakTime.toLong(), textView)     // set break timer

            binding.buttonSecond.setOnClickListener {
                Intent(requireContext().applicationContext, TimerService::class.java).also {
                    it.action = TimerService.Actions.START.toString()
                    it.putExtra("workoutTime", workout.workoutTime.toLong())
                    it.putExtra("breakTime", workout.breakTime.toLong())
                    it.putExtra("totalSets", workout.numSets)
                    requireContext().startService(it)
                }

                binding.buttonSecond.visibility = View.GONE
                binding.resetButton.visibility = View.VISIBLE

                warmUpTimer.start()
                var mediaPlayer = MediaPlayer.create(context, breakSounds[0])
                mediaPlayer.setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                }
                mediaPlayer.start()
            }
        }

        binding.resetButton.setOnClickListener {
            Intent(requireContext().applicationContext, TimerService::class.java).also {
                it.action = TimerService.Actions.RESET.toString()
                requireContext().startService(it)
            }

            // cancel running timers
            warmUpTimer.cancel()
            woTimer.cancel()
            breakTimer.cancel()

            // reset the workout
            numSetsRemaining = numSets
            binding.textviewTimer.text = getString(R.string.initial_time)
            binding.tvWoInfo.text = getString(R.string.warmup)
            updateSetInfo()

            // show relevant button
            binding.resetButton.visibility = View.GONE
            binding.buttonSecond.visibility = View.VISIBLE
        }
    }

    private fun createWarmUpTimer(textView: TextView): CountDownTimer {
        return object : CountDownTimer(10 * 1000, 1) {

            override fun onTick(millisUntilFinished: Long) {
                textView.text = (millisUntilFinished / 1000).toString()
                if (millisUntilFinished.toInt() in 5210..5250) {
                    var mediaPlayer = MediaPlayer.create(context, R.raw.time_signals)
                    mediaPlayer.setOnCompletionListener {
                        it.release()
                        mediaPlayer = null
                    }
                    mediaPlayer.start()
                }
            }

            override fun onFinish() {
                updateRoutineInfoToWo()
                woTimer.start()
                var mediaPlayer = MediaPlayer.create(context, woSounds[0])
                mediaPlayer.setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                }
                mediaPlayer.start()
            }
        }
    }

    private fun createBreakTimer(timeInSeconds: Long, textView: TextView): CountDownTimer {
        return object : CountDownTimer(timeInSeconds * 1000, 1) {

            override fun onTick(millisUntilFinished: Long) {
                textView.text = (millisUntilFinished / 1000).toString()
                if (millisUntilFinished.toInt() in 5210..5250) {
                    var mediaPlayer = MediaPlayer.create(context, R.raw.time_signals)
                    mediaPlayer.setOnCompletionListener {
                        it.release()
                        mediaPlayer = null
                    }
                    mediaPlayer.start()
                }
            }

            override fun onFinish() {
                updateRoutineInfoToWo()
                woTimer.start()
                var mediaPlayer = MediaPlayer.create(context, woSounds[numSets-numSetsRemaining])
                mediaPlayer.setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                }
                mediaPlayer.start()
            }
        }
    }

    private fun createWoTimer(timeInSeconds: Long, textView: TextView): CountDownTimer {
        return object : CountDownTimer(timeInSeconds * 1000, 1) {

            override fun onTick(millisUntilFinished: Long) {
                textView.text = (millisUntilFinished / 1000).toString()
                if (millisUntilFinished.toInt() in 5210..5250) {
                    var mediaPlayer = MediaPlayer.create(context, R.raw.time_signals)
                    mediaPlayer.setOnCompletionListener {
                        it.release()
                        mediaPlayer = null
                    }
                    mediaPlayer.start()
                }
            }

            override fun onFinish() {
                numSetsRemaining--
                updateSetInfo()
                if (numSetsRemaining == 0) {
                    textView.text = getString(R.string.workoutFinishedText)
                    var mediaPlayer = MediaPlayer.create(context, R.raw.workout_completed)
                    mediaPlayer.setOnCompletionListener {
                        it.release()
                        mediaPlayer = null
                    }
                    mediaPlayer.start()
                } else {
                    updateRoutineInfoToBreak()
                    breakTimer.start()
                    var mediaPlayer = MediaPlayer.create(context, breakSounds[numSets-numSetsRemaining])
                    mediaPlayer.setOnCompletionListener {
                        it.release()
                        mediaPlayer = null
                    }
                    mediaPlayer.start()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        warmUpTimer.cancel()
        woTimer.cancel()
        breakTimer.cancel()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        warmUpTimer.cancel()
        woTimer.cancel()
        breakTimer.cancel()
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