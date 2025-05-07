package com.example.terravive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.terravive.ActivityItemModel
import com.example.terravive.R

class OrganizerActivityAdapter(
    private val activities: List<ActivityItemModel>, // List of ActivityItemModel
    private val onEditClick: (ActivityItemModel) -> Unit,
    private val onDeleteClick: (ActivityItemModel) -> Unit
) : RecyclerView.Adapter<OrganizerActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout (item_organizer_activity.xml)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_organizer_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]

        // Bind data from ActivityItemModel to views
        holder.tvName.text = activity.name
        holder.tvDate.text = "Date: ${activity.date}" // Assuming date is a String; adjust if it's a Date object
        holder.tvLocation.text = "Location: ${activity.location}"

        // Set click listeners for edit and delete buttons
        holder.btnEdit.setOnClickListener { onEditClick(activity) }
        holder.btnDelete.setOnClickListener { onDeleteClick(activity) }
    }

    override fun getItemCount(): Int = activities.size

    // ViewHolder class to hold references to the views in the item layout
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvActivityName)
        val tvDate: TextView = itemView.findViewById(R.id.tvActivityDate)
        val tvLocation: TextView = itemView.findViewById(R.id.tvActivityLocation)
        val btnEdit: Button = itemView.findViewById(R.id.btnEditActivity)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteActivity)
    }
}
