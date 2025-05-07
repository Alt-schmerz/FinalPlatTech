package com.example.terravive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(
    private val context: Context,
    private val notifications: List<NotificationItem>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notifications[position]

        // Set profile image
        holder.profileImage.setImageResource(item.profileImageResource)

        // Set notification text
        val notificationText = "${item.username} ${item.message}"
        holder.notificationText.text = notificationText

        // Set content image
        holder.contentImage.setImageResource(item.contentImageResource)
    }

    override fun getItemCount(): Int = notifications.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val notificationText: TextView = itemView.findViewById(R.id.notificationText)
        val contentImage: ImageView = itemView.findViewById(R.id.contentImage)
    }
}
