package com.example.terravive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.terravive.model.ActivityEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.activityRecyclerView)

        // Empty list to start, will be populated with approved activities from Firestore
        val activities = mutableListOf<ActivityEvent>()

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Adapter for the RecyclerView
        val activityAdapter = object : RecyclerView.Adapter<ActivityViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_activity, parent, false)
                return ActivityViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
                val item = activities[position]
                holder.tvName.text = item.name
                holder.tvDate.text = "Date: ${item.date}"
                holder.tvTime.text = "Time: ${item.time}"
                holder.tvLocation.text = "Location: ${item.location}"

                // Set the image based on the event name or ID
                when (item.name) {
                    "Trash Picking" -> holder.imgEvent.setImageResource(R.drawable.burnham)
                    "Eco Seminar" -> holder.imgEvent.setImageResource(R.drawable.sunshine)
                    "Gutter Cleaning" -> holder.imgEvent.setImageResource(R.drawable.cjh)
                    "Community Clean-Up" -> holder.imgEvent.setImageResource(R.drawable.burnham)
                    "Plastic Sorting Drive" -> holder.imgEvent.setImageResource(R.drawable.cjh)
                    else -> holder.imgEvent.setImageResource(R.drawable.leaf_icon) // fallback image
                }

                val userEmail = auth.currentUser?.email ?: return

                val userDocRef = db.collection("activities_approved")
                    .document(item.id)  // Using id here since it's the approved activities collection
                    .collection("users")
                    .document(userEmail)

                userDocRef.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val timestamp = document.getLong("timestamp") ?: 0L
                        val elapsedTime = System.currentTimeMillis() - timestamp

                        if (elapsedTime < 180000) { // < 3 minutes
                            holder.btnReserve.text = "Reserved (Undo)"
                            holder.btnReserve.isEnabled = true

                            holder.btnReserve.setOnClickListener {
                                userDocRef.delete().addOnSuccessListener {
                                    Toast.makeText(context, "Reservation cancelled.", Toast.LENGTH_SHORT).show()
                                    holder.btnReserve.text = "Reserve"
                                    holder.btnReserve.setOnClickListener {
                                        reserveUserUnderActivity(item, userEmail, holder)
                                    }
                                }
                            }
                        } else {
                            holder.btnReserve.text = "Reserved"
                            holder.btnReserve.isEnabled = false
                        }
                    } else {
                        holder.btnReserve.text = "Reserve"
                        holder.btnReserve.isEnabled = true
                        holder.btnReserve.setOnClickListener {
                            reserveUserUnderActivity(item, userEmail, holder)
                        }
                    }
                }
            }

            override fun getItemCount(): Int = activities.size
        }

        recyclerView.adapter = activityAdapter

        // Fetch approved activities from Firestore
        loadApprovedActivities(activities, activityAdapter)

        return view
    }

    private fun loadApprovedActivities(activities: MutableList<ActivityEvent>, activityAdapter: RecyclerView.Adapter<ActivityViewHolder>) {
        val userEmail = auth.currentUser?.email ?: return

        // Get approved activities from Firestore
        db.collection("activities_approved")
            .get()
            .addOnSuccessListener { documents ->
                activities.clear()  // Clear the list before adding new activities
                for (document in documents) {
                    val activity = document.toObject(ActivityEvent::class.java)
                    activity.id = document.id  // Ensure the ID is attached to the activity
                    activities.add(activity)
                }
                activityAdapter.notifyDataSetChanged()  // Notify the adapter to update the UI
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error fetching activities: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun reserveUserUnderActivity(item: ActivityEvent, userEmail: String, holder: ActivityViewHolder) {
        val userData = hashMapOf(
            "timestamp" to System.currentTimeMillis(),
            "email" to userEmail
        )

        val userDocRef = db.collection("activities_approved")
            .document(item.id)  // Approved activities collection
            .collection("users")
            .document(userEmail)

        userDocRef.set(userData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Reserved: ${item.name}", Toast.LENGTH_SHORT).show()
                holder.btnReserve.text = "Reserved (Undo)"
                holder.btnReserve.isEnabled = true

                holder.btnReserve.setOnClickListener {
                    userDocRef.delete().addOnSuccessListener {
                        Toast.makeText(context, "Reservation cancelled.", Toast.LENGTH_SHORT).show()
                        holder.btnReserve.text = "Reserve"
                        holder.btnReserve.setOnClickListener {
                            reserveUserUnderActivity(item, userEmail, holder)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        val btnReserve: Button = view.findViewById(R.id.btnReserve)
        val imgEvent: ImageView = view.findViewById(R.id.imgEvent)
    }
}

