package com.example.terravive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class OrganizerProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_organizer_profile, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val user = auth.currentUser
        val profileImage = view.findViewById<ImageView>(R.id.profile_image)
        val nameText = view.findViewById<TextView>(R.id.profile_name)
        val emailText = view.findViewById<TextView>(R.id.profile_email)
        val signOutButton = view.findViewById<Button>(R.id.sign_out_button)
        val viewActivitiesBtn = view.findViewById<Button>(R.id.view_activities_button)

        // Load profile info
        user?.let {
            emailText.text = it.email

            db.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        nameText.text = document.getString("name") ?: "No Name"
                        val imageUrl = document.getString("imageUrl")
                        if (!imageUrl.isNullOrEmpty()) {
                            Glide.with(this).load(imageUrl).into(profileImage)
                        }
                    }
                }
        }

        // Sign out
        signOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }

        // View published activities
        viewActivitiesBtn.setOnClickListener {
            val uid = auth.currentUser?.uid ?: return@setOnClickListener
            db.collection("activities_approved")
                .whereEqualTo("creatorId", uid)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(requireContext(), "No approved activities found.", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val builder = StringBuilder()
                    for (doc in documents) {
                        builder.append("\nTitle: ${doc.getString("title")}\n")
                        builder.append("Description: ${doc.getString("description")}\n")

                        val imageUrl = doc.getString("imageUrl") ?: ""

                        // Show users reserved under the activity
                        db.collection("activities_approved")
                            .document(doc.id)
                            .collection("users")
                            .get()
                            .addOnSuccessListener { users ->
                                builder.append("Reserved by:\n")
                                for (userDoc in users) {
                                    builder.append("- ${userDoc.getString("name")} (${userDoc.getString("email")})\n")
                                }

                                // Show final details in a dialog
                                val dialog = AlertDialog.Builder(requireContext())
                                    .setTitle("Activity Details")
                                    .setMessage(builder.toString())
                                    .setPositiveButton("OK", null)
                                    .create()
                                dialog.show()
                            }
                    }
                }
        }

        return view
    }
}
