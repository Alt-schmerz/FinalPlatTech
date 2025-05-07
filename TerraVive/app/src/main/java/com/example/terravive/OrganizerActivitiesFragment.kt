package com.example.terravive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.terravive.ActivitiesItemModel
import com.example.terravive.ActivityItemModel
import com.example.terravive.R
import com.example.terravive.adapter.ActivityAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference

class OrganizerActivitiesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActivityAdapter
    private val activityList = mutableListOf<ActivityItemModel>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_organizer_activities, container, false)

        // Initialize the recyclerView
        recyclerView = view.findViewById(R.id.recyclerViewActivities)

        // Initialize the adapter with activityList and an onClick listener for deleting activities
        adapter = ActivityAdapter(
            activityList,
            { activity -> deleteActivity(activity) }, // onDelete click
            { activity -> approveActivity(activity) } // onApprove click
        )

        // Set the layout manager and adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        loadActivities()

        return view
    }

    private fun loadActivities() {
        // Fetch activities from Firestore
        db.collection("activities_approved").get().addOnSuccessListener { result ->
            for (doc in result) {
                val item = doc.toObject(ActivityItemModel::class.java)
                activityList.add(item)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun deleteActivity(activity: ActivityItemModel) {
        // Fetch the document reference based on the activity ID
        val activityRef: DocumentReference = db.collection("activities_approved").document(activity.id)

        // Delete the activity from Firestore
        activityRef.delete().addOnSuccessListener {
            // Notify the user
            // You can add a Toast or Snackbar for confirmation
            // For example:
            // Toast.makeText(context, "Activity deleted", Toast.LENGTH_SHORT).show()

            // Remove the activity from the local list
            activityList.remove(activity)
            adapter.notifyDataSetChanged() // Refresh the list
        }.addOnFailureListener { e ->
            // Handle any error
            // For example, show a Toast with the error message
            // Toast.makeText(context, "Error deleting activity: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun approveActivity(activity: ActivityItemModel) {
        // For demonstration purposes, you can update the activity's approval status
        // assuming the ActivityItemModel has a field "isApproved"
        val updatedActivity = activity.copy(isApproved = true)

        // Update the Firestore document for the approved activity
        val activityRef: DocumentReference = db.collection("activities_approved").document(activity.id)
        activityRef.set(updatedActivity).addOnSuccessListener {
            // Notify the user
            // Toast.makeText(context, "Activity approved", Toast.LENGTH_SHORT).show()

            // Update the activity list and notify the adapter
            val index = activityList.indexOf(activity)
            if (index != -1) {
                activityList[index] = updatedActivity
                adapter.notifyItemChanged(index)
            }
        }.addOnFailureListener { e ->
            // Handle any error
            // Toast.makeText(context, "Error approving activity: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
