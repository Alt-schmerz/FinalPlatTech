package com.example.terravive

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OrganizerDashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_dashboard)

        val btnCreateEvent = findViewById<Button>(R.id.btnCreateEvent)
        val btnViewMyActivities = findViewById<Button>(R.id.btnViewMyActivities)

        btnCreateEvent.setOnClickListener {
          //  startActivity(Intent(this, CreateActivityEvent::class.java)) // Make this activity later
        }

        btnViewMyActivities.setOnClickListener {
            //startActivity(Intent(this, OrganizerActivityList::class.java)) // Make this activity later
        }
    }
}
