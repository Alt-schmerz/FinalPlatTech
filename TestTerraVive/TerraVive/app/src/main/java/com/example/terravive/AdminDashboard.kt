package com.example.terravive

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.terravive.databinding.ActivityDashboardBinding

class AdminDashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Default fragment when activity starts (Admin Dashboard)
        replaceFragment(AdminHomeFragment())

        // Set up the bottom navigation listener
        binding.adminBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_dashboard -> {
                    replaceFragment(AdminHomeFragment())
                    true
                }
                R.id.admin_activities -> {
                    replaceFragment(AdminActivityFragment())
                    true
                }
                R.id.reports -> {
                    replaceFragment(NotificationFragment())
                    true
                }
                R.id.admin_profile -> {
                    replaceFragment(ProfileFragment())
                   true

                }
                else -> false
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
