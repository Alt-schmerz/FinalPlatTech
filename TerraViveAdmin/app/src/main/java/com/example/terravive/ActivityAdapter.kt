package com.example.terravive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.terravive.R
import com.example.terravive.ActivityItemModel

class ActivityAdapter(
    private val activityList: MutableList<ActivityItemModel>, // MutableList to allow updates
    private val onApproveClick: (ActivityItemModel) -> Unit,
    private val onDenyClick: (ActivityItemModel) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.activityNameTextView)
        val dateText: TextView = itemView.findViewById(R.id.activityDateTextView)
        val statusText: TextView = itemView.findViewById(R.id.activityStatusTextView)
        val approveButton: Button = itemView.findViewById(R.id.approveButton)
        val denyButton: Button = itemView.findViewById(R.id.denyButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activityList[position]
        holder.nameText.text = "Name: ${activity.name}"
        holder.dateText.text = "Date: ${activity.date}"  // Display date
        holder.statusText.text = "Status: ${activity.status}"  // Display status

        holder.approveButton.setOnClickListener {
            onApproveClick(activity)
        }

        holder.denyButton.setOnClickListener {
            onDenyClick(activity)
        }
    }

    override fun getItemCount(): Int = activityList.size

    // This function ensures the adapter can update the data and refresh the view
    fun updateActivities(newActivities: List<ActivityItemModel>) {
        activityList.clear()
        activityList.addAll(newActivities)
        notifyDataSetChanged()
    }
}
