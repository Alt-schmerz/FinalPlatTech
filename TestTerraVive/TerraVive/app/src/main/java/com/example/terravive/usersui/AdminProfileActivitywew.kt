package com.example.terravive

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.terravive.databinding.ActivityAdminProfileBinding

class AdminProfileActivitywew : AppCompatActivity() {

    private lateinit var binding: ActivityAdminProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views
        val profileImageView: ImageView = findViewById(R.id.profileImageView)
        val adminNameTextView: TextView = findViewById(R.id.adminNameTextView)
        val adminEmailTextView: TextView = findViewById(R.id.adminEmailTextView)
        val adminRoleTextView: TextView = findViewById(R.id.adminRoleTextView)
        //val editProfileButton: Button = findViewById(R.id.editProfileButton)
       // val logoutButton: Button = findViewById(R.id.logoutButton)

        // Set data to views (these can come from Firebase or any data source)
        adminNameTextView.text = "Admin Name" // Replace with dynamic data
        adminEmailTextView.text = "admin@example.com" // Replace with dynamic data
        adminRoleTextView.text = "Role: Admin" // Replace with dynamic data

        // Set up edit profile button click listener
        //editProfileButton.setOnClickListener {
            // Navigate to Edit Profile screen (create a new activity if necessary)
            // For example, use Intent to start the EditProfileActivity
        }

        // Set up logout button click listener
      //  logoutButton.setOnClickListener {
            // Handle logout logic here
            // For example, sign out from Firebase or clear session and navigate to login screen
     //   }
    //}
}
