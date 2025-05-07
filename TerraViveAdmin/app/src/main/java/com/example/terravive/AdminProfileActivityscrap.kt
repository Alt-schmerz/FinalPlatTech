package com.example.terravive

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminProfileActivityscrap : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var adminNameTextView: TextView
    private lateinit var adminEmailTextView: TextView
    private lateinit var adminRoleTextView: TextView
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView)
        adminNameTextView = findViewById(R.id.adminNameTextView)
        adminEmailTextView = findViewById(R.id.adminEmailTextView)
        adminRoleTextView = findViewById(R.id.adminRoleTextView)
        signOutButton = findViewById(R.id.signOutButton)

        // Set data to views (these can come from Firebase or any data source)
        adminNameTextView.text = "Admin Name" // Replace with dynamic data (e.g., Firebase)
        adminEmailTextView.text = FirebaseAuth.getInstance().currentUser?.email ?: "admin@example.com" // Get current user's email from Firebase
        adminRoleTextView.text = "Role: Admin" // You can adjust this depending on the role

        // Set up Sign Out button click listener
        signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut() // Sign out from Firebase

        // Navigate to the login screen (or any other screen you want to show after signing out)
        val intent = Intent(this, MainActivity::class.java) // Replace with your Login Activity
        startActivity(intent)
        finish() // Finish the current activity to prevent returning to the profile screen
    }
}
