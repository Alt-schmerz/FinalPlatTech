package com.example.terravive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ApprovedActivityAdapter(
    private val context: Context,
    private val activities: List<ApprovedActivity>
) : RecyclerView.Adapter<ApprovedActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_approved_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]
        holder.dateText.text = "Date: ${activity.date}"
        holder.locationText.text = "Location: ${activity.location}"
        holder.nameText.text = "Name: ${activity.name}"
        holder.timeText.text = "Time: ${activity.time}"
    }

    override fun getItemCount(): Int = activities.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val locationText: TextView = itemView.findViewById(R.id.locationText)
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val timeText: TextView = itemView.findViewById(R.id.timeText)
    }
}
