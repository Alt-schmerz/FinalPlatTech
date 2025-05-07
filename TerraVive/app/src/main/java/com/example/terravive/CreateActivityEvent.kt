package com.example.terravive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// CreateActivityEvent.kt
class CreateActivityEvent : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var etLocation: EditText
    private lateinit var btnSubmit: Button
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_activity_event)

        etName = findViewById(R.id.etName)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        etLocation = findViewById(R.id.etLocation)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val name = etName.text.toString()
            val date = etDate.text.toString()
            val time = etTime.text.toString()
            val location = etLocation.text.toString()
            val email = auth.currentUser?.email ?: "unknown"

            if (name.isBlank() || date.isBlank() || time.isBlank() || location.isBlank()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = db.collection("activities_pending").document().id
            val activity = hashMapOf(
                "id" to id,
                "name" to name,
                "date" to date,
                "time" to time,
                "location" to location,
                "organizer" to email,
                "status" to "pending"
            )

            db.collection("activities_pending").document(id).set(activity)
                .addOnSuccessListener {
                    Toast.makeText(this, "Activity submitted for approval!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}