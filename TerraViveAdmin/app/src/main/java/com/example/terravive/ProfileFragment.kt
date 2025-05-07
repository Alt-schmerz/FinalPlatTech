package com.example.terravive

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // UI elements
        val nameTextView = view.findViewById<TextView>(R.id.tv_name)
        val emailTextView = view.findViewById<TextView>(R.id.tv_email)
        val profileImageView = view.findViewById<ImageView>(R.id.user_profile_image)
        val signOutButton = view.findViewById<Button>(R.id.sign_out_button)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // Get user info
        val firebaseUser = firebaseAuth.currentUser
        val googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext())

        if (firebaseUser != null) {
            nameTextView.text = firebaseUser.displayName ?: "No Name"
            emailTextView.text = firebaseUser.email ?: "No Email"
            firebaseUser.photoUrl?.let {
                Glide.with(this).load(it).into(profileImageView)
            }
        } else if (googleAccount != null) {
            nameTextView.text = googleAccount.displayName ?: "No Name"
            emailTextView.text = googleAccount.email ?: "No Email"
            googleAccount.photoUrl?.let {
                Glide.with(this).load(it).into(profileImageView)
            }
        } else {
            nameTextView.text = "No Name"
            emailTextView.text = "No Email"
        }

        // Sign out logic
        signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            googleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        return view
    }
}
