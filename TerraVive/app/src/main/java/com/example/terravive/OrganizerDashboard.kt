package com.example.terravive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.terravive.R
import com.example.terravive.OrganizerActivitiesFragment
import com.example.terravive.OrganizerCharityFragment
import com.example.terravive.OrganizerHomeFragment
import com.example.terravive.OrganizerProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.terravive.databinding.ActivityDashboardBinding
import com.example.terravive.databinding.OrganizerDashboardBinding

class OrganizerDashboard : AppCompatActivity() {

    private lateinit var binding: OrganizerDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OrganizerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(OrganizerHomeFragment())

        binding.organizerBottomMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(OrganizerProfileFragment())
                    true
                }

                R.id.nav_activities -> {
                    replaceFragment(OrganizerActivitiesFragment())
                    true
                }

                R.id.nav_charity -> {
                    replaceFragment(OrganizerCharityFragment())
                    true
                }

                R.id.nav_notifications -> {
                    replaceFragment(OrganizerNotificationsFragment())
                    true
                }

                R.id.nav_profile -> {
                    replaceFragment(OrganizerProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.organizer_home, fragment)
            .commit()
    }

}
