package com.example.terravive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.terravive.adapter.ActivityAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivityFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var activitiesList: MutableList<ActivityItemModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_activity, container, false)

        recyclerView = view.findViewById(R.id.activityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        activitiesList = mutableListOf()
        activityAdapter = ActivityAdapter(activitiesList, ::onApproveClick, ::onDenyClick)
        recyclerView.adapter = activityAdapter

        val seedButton: Button = view.findViewById(R.id.seedActivitiesButton) // Fixed here
        seedButton.setOnClickListener {
            seedSampleActivities()
        }

        loadActivities()

        return view
    }

    private fun seedSampleActivities() {
        val db = FirebaseFirestore.getInstance()

        val activities = listOf(
            ActivityItemModel("1", "Trash Picking", "2025-06-18", "10:00 AM", "Burnham Park", "", "pending"),
            ActivityItemModel("2", "Eco Seminar", "2025-06-11", "11:00 AM", "Sunshine Park", "", "pending"),
            ActivityItemModel("3", "Gutter Cleaning", "2025-06-10", "9:00 AM", "Camp John Hay", "", "pending"),
            ActivityItemModel("4", "Community Clean-Up", "2025-06-12", "8:00 AM", "Riverfront", "", "pending"),
            ActivityItemModel("5", "Plastic Sorting Drive", "2025-06-15", "1:00 PM", "City Hall Grounds", "", "pending")
        )

        for (activity in activities) {
            db.collection("activities_pending")
                .document(activity.id)
                .set(activity)
                .addOnSuccessListener {
                    Toast.makeText(context, "Seeded: ${activity.name}", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun loadActivities() {
        val db = FirebaseFirestore.getInstance()
        db.collection("activities_pending")
            .get()
            .addOnSuccessListener { documents ->
                activitiesList.clear()
                for (document in documents) {
                    val activity = document.toObject(ActivityItemModel::class.java)
                    activity.id = document.id  // Make sure 'id' is assigned for transfer
                    activitiesList.add(activity)
                }
                activityAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error fetching activities: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onApproveClick(activity: ActivityItemModel) {
        val db = FirebaseFirestore.getInstance()

        // Change the status to "approved"
        activity.status = "approved"

        db.collection("activities_approved")
            .document(activity.id)
            .set(activity)
            .addOnSuccessListener {
                // Remove from 'activities_pending'
                db.collection("activities_pending")
                    .document(activity.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Activity approved!", Toast.LENGTH_SHORT).show()
                        loadActivities()  // Reload activities after update
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to delete from pending: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to approve: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onDenyClick(activity: ActivityItemModel) {
        val db = FirebaseFirestore.getInstance()

        // Change the status to "denied"
        activity.status = "denied"

        db.collection("activities_denied")
            .document(activity.id)
            .set(activity)
            .addOnSuccessListener {
                // Remove from 'activities_pending'
                db.collection("activities_pending")
                    .document(activity.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Activity denied!", Toast.LENGTH_SHORT).show()
                        loadActivities()  // Reload activities after update
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to delete from pending: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to deny: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}



