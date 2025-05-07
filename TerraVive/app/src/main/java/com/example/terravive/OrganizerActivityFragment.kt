package com.example.terravive
import android.widget.Toast
import com.example.terravive.ActivityItemModel
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.terravive.adapter.ActivityAdapter

class OrganizerActivityFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var activitiesList: MutableList<ActivityItemModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_organizer_activity, container, false)

        recyclerView = view.findViewById(R.id.activityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        activitiesList = mutableListOf()

        // Pass only delete function; no approve/deny needed for organizers
        activityAdapter = ActivityAdapter(activitiesList, {}, {}, ::onDeleteClick)
        recyclerView.adapter = activityAdapter

        loadActivities()

        return view
    }

    private fun loadActivities() {
        val db = FirebaseFirestore.getInstance()
        db.collection("activities_approved")
            .get()
            .addOnSuccessListener { documents ->
                activitiesList.clear()
                for (document in documents) {
                    val activity = document.toObject(ActivityItemModel::class.java)
                    activity.id = document.id
                    activitiesList.add(activity)
                }
                activityAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error loading activities: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onDeleteClick(activity: ActivityItemModel) {
        val db = FirebaseFirestore.getInstance()
        db.collection("activities_approved")
            .document(activity.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Activity deleted", Toast.LENGTH_SHORT).show()
                loadActivities()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to delete: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
