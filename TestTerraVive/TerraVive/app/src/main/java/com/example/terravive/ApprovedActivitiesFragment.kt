package com.example.terravive

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.terravive.databinding.FragmentApprovedActivitiesBinding
import com.google.firebase.firestore.FirebaseFirestore

class ApprovedActivitiesFragment : Fragment() {

    private var _binding: FragmentApprovedActivitiesBinding? = null
    private val binding get() = _binding!!

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApprovedActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchApprovedActivities()
    }

    private fun fetchApprovedActivities() {
        firestore.collection("activities_approved")
            .get()
            .addOnSuccessListener { result ->
                val builder = StringBuilder()
                for (document in result) {
                    val activity = document.toObject(ApprovedActivity::class.java)
                    builder.append("Date: ${activity.date}\n")
                    builder.append("Location: ${activity.location}\n")
                    builder.append("Name: ${activity.name}\n")
                    builder.append("Time: ${activity.time}\n")
                    builder.append("----------------------\n")
                }
                binding.activitiesTextView.text = builder.toString()
            }
            .addOnFailureListener { exception ->
                Log.e("ApprovedActivities", "Error getting documents: ", exception)
                binding.activitiesTextView.text = "Failed to load activities."
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
