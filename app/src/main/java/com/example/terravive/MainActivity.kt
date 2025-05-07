package com.example.terravive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener // Declare the listener

    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize the AuthStateListener
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null && user.email != null) {
                // User is now signed in and email is available
                updateUI(user)
                // Remove the listener to prevent multiple calls
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }

        // **Modified Code:**
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null && currentUser.email != null) {
            // User is already signed in and email is available
            updateUI(currentUser)
        } else {
            // User is not signed in or email is not available
            // Add the auth state listener
            firebaseAuth.addAuthStateListener(authStateListener)
        }

        // UI Components (rest of your onCreate code remains the same)
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_button)
        val googleSignInButton: ImageView = findViewById(R.id.google_sign_in_button)
        val registerButton: Button = findViewById(R.id.create_an_acc)

        // Google Sign-In configuration
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Standard login flow
        loginBtn.setOnClickListener {
            val email = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        updateUI(firebaseAuth.currentUser)
                    } else {
                        Log.w("Login", "signInWithEmail:failure", task.exception)
                        val exception = task.exception
                        when (exception) {
                            is FirebaseAuthInvalidUserException -> {
                                // Account not found, show message and redirect to Register activity
                                Toast.makeText(this, "Account not found. Please register first.", Toast.LENGTH_LONG).show()
                                // Redirect to register screen
                                startActivity(Intent(this, Register::class.java))
                                finish()
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                // Wrong password or badly formatted email
                                Toast.makeText(
                                    this,
                                    "Invalid credentials. Please check your email and password.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else -> {
                                // Generic error handling
                                Toast.makeText(this, "Authentication failed: ${exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                            }
                        }
                        updateUI(null)
                    }
                }
        }

        // Google Sign-In button logic
        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


        // Navigation to Register activity
        registerButton.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if the user is already signed in (we might not need this anymore with the listener)
        // val user = firebaseAuth.currentUser
        // if (user != null) {
        //     // User is signed in, redirect to Dashboard
        //     updateUI(user)
        // }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("SignInError", "Google sign in failed, code: ${e.statusCode}", e)
                val errorMsg = when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> "Sign-In was canceled by the user."
                    else -> "Sign-In failed: ${e.statusCode}"
                }
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateUI(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(this, "Firebase authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        Log.d("MainActivity", "updateUI called with user: $user")
        Log.d("MainActivity", "User email: ${user?.email}")
        if (user == null || user.email == null) {
            Toast.makeText(this, "Invalid user.", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        val email = user.email!! // Use the non-null email here
        Log.d("MainActivity", "Attempting to fetch user data for email: $email")

        db.collection("users").document(email).get()  // Corrected line
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val role = document.getString("role")
                    when (role) {
                        "admin" -> {
                            Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, AdminDashboard::class.java))
                        }
                        "organizer" -> {
                            Toast.makeText(this, "Welcome Organizer!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, OrganizerDashboard::class.java))
                        }
                        "client" -> {
                            Toast.makeText(this, "Welcome Client!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Dashboard::class.java))
                        }
                        else -> {
                            // Redirect to HomeFragment if role is unknown
                            startActivity(Intent(this, HomeFragment::class.java))
                            Toast.makeText(this, "Unknown role. Redirecting to Home.", Toast.LENGTH_LONG).show()
                        }
                    }
                    finish()
                } else {
                    // If no user data found, redirect to HomeFragment
                    startActivity(Intent(this, HomeFragment::class.java))
                    Toast.makeText(this, "No user data found. Redirecting to Home.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching user role: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Don't forget to remove the listener in onDestroy to prevent memory leaks
    override fun onDestroy() {
        super.onDestroy()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        TODO("Not yet implemented")
    }
}

