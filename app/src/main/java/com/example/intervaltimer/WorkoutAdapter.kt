package com.example.intervaltimer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.collections.immutable.PersistentList

class WorkoutAdapter(private val dataSet: PersistentList<WorkoutStore>) :
    RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val action = SelectWorkoutFragmentDirections.actionFirstFragmentToSecondFragment(position)
                    it.findNavController().navigate(action)
                }
            }
            view.setOnLongClickListener {
                val delButton = view.findViewById<ImageButton>(R.id.delWoButton)
                delButton.visibility = View.VISIBLE
                true
            }
            textView = view.findViewById(R.id.tvListItem)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val woStr = "${convertSecondsToString(dataSet[position].workoutTime)}/${convertSecondsToString(dataSet[position].breakTime)}/${dataSet[position].numSets}"
        viewHolder.textView.text = woStr
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    private fun convertSecondsToString(data: Int): String{
        val seconds = data % 60
        val minutes = (data - seconds) / 60
        val secStr = if (seconds >= 10) {
            "$seconds"
        } else {
            "0$seconds"
        }
        val minStr = if (minutes >= 10) {
            "$minutes"
        } else {
            "0$minutes"
        }
        return "$minStr:$secStr"
    }

}

