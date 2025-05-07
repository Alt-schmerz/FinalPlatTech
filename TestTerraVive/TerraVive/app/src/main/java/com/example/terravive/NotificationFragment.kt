package com.example.terravive

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terravive.databinding.FragmentNotificationBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityAdapter: ApprovedActivityAdapter
    private val activities = mutableListOf<ApprovedActivity>()

    private val firestore = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        activityAdapter = ApprovedActivityAdapter(requireContext(), activities)
        binding.notificationsRecyclerView.adapter = activityAdapter

        loadApprovedActivities()
    }

    private fun loadApprovedActivities() {
        listenerRegistration?.remove()

        val collectionRef = firestore.collection("activities_approved")

        listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("NotificationFragment", "Firestore listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                activities.clear()
                for (doc in snapshot.documents) {
                    val activity = doc.toObject(ApprovedActivity::class.java)
                    if (activity != null) {
                        activities.add(activity)
                    }
                }
                activityAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listenerRegistration?.remove()
        _binding = null
    }
}
