package com.example.terravive

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrganizerActivityList : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrganizerActivityAdapter
    private lateinit var activityList: MutableList<ActivityItemModel>  // Update the list to hold ActivityItemModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_activity_list)

        recyclerView = findViewById(R.id.organizerActivityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        activityList = mutableListOf()

        adapter = OrganizerActivityAdapter(
            activities = activityList,  // Pass ActivityItemModel list to the adapter
            onEditClick = { activity ->
                Toast.makeText(this, "Edit clicked for ${activity.name}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { activity ->
                db.collection("activities_approved")
                    .document(activity.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Activity deleted", Toast.LENGTH_SHORT).show()
                        loadOrganizerActivities()  // Reload activities after deletion
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        )
        recyclerView.adapter = adapter

        loadOrganizerActivities()
    }

    private fun loadOrganizerActivities() {
        val email = auth.currentUser?.email ?: return
        db.collection("activities_approved")
            .whereEqualTo("organizer", email)
            .get()
            .addOnSuccessListener { docs ->
                activityList.clear()
                for (doc in docs) {
                    val item = ActivityItemModel(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        date = doc.getString("date") ?: "",
                        time = doc.getString("time") ?: "",
                        location = doc.getString("location") ?: "",
                        description = doc.getString("description") ?: "",
                        status = doc.getString("status") ?: ""
                    )
                    activityList.add(item)  // Add ActivityItemModel to the list
                }
                adapter.notifyDataSetChanged()  // Notify the adapter to update the UI
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load activities.", Toast.LENGTH_SHORT).show()
            }
    }
}
