package com.example.terravive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class DonorFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_donors, container, false)

        // Find the back button
        val backButton = view.findViewById<Button>(R.id.backButton)

        // Set click listener for the back button
        backButton?.setOnClickListener {
            // Use parentFragmentManager to navigate back
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
