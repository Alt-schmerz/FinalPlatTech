package com.example.terravive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.terravive.model.ActivityEvent

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var expandButtonLL: LinearLayout
    private lateinit var donorsButtonLL: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        expandButtonLL = view.findViewById(R.id.expand_activities_button) ?: run {
            // Handle the error: log it, show a message, or return null to prevent further errors
            return null
        }
        donorsButtonLL = view.findViewById(R.id.donors_button) ?: run {
            // Handle the error
            return null
        }

        recyclerView = view.findViewById(R.id.activities_recycler_view) ?: run {
            return null
        }

        expandButtonLL.setOnClickListener {
            navigateToActivityFragment()
        }
        donorsButtonLL.setOnClickListener {
            navigateToDonorFragment()
        }

       recyclerView.setOnClickListener {
            navigateToActivityFragment()
        }

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val activities = listOf(
            ActivityEvent("1", "Trash Picking", "2025-06-18", "10:00 AM", "Burnham Park", R.drawable.burnham),
            ActivityEvent("2", "Eco Seminar", "2025-06-11", "11:00 AM", "Sunshine Park", R.drawable.sunshine),
            ActivityEvent("3", "Gutter Cleaning", "2025-06-10", "9:00 AM", "Camp John Hay", R.drawable.cjh),
            ActivityEvent("4", "Community Clean-Up", "2025-06-12", "8:00 AM", "Burnham Park", R.drawable.burnham),
            ActivityEvent("5", "Plastic Sorting Drive", "2025-06-15", "1:00 PM", "Camp John Hay", R.drawable.cjh)
        )
        activityAdapter = ActivityAdapter(activities, this) // Pass the fragment instance
        recyclerView.adapter = activityAdapter


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun navigateToActivityFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.home_layout, ActivityFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToDonorFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.home_layout, DonorFragment())
            .addToBackStack(null)
            .commit()
    }

    class ActivityAdapter(
        private val activities: List<ActivityEvent>,
        private val fragment: Fragment // Pass the fragment to the adapter
    ) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

        class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgEvent: ImageView = itemView.findViewById(R.id.imgEvent)
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
            val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_act_display, parent, false)
            return ActivityViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
            val activity = activities[position]
            holder.imgEvent.setImageResource(activity.image)
            holder.tvName.text = activity.name
            holder.tvLocation.text = activity.location
            holder.tvTime.text = activity.time

            // Set click listener for the item
            holder.itemView.setOnClickListener {
                // Navigate to ActivityFragment
                val activityFragment = ActivityFragment() // Create an instance of ActivityFragment
                fragment.parentFragmentManager.beginTransaction()
                    .replace(R.id.home_layout, activityFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        override fun getItemCount(): Int = activities.size
    }


}
