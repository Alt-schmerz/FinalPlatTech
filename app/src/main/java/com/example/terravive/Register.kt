package com.example.terravive

import android.util.Log
import android.os.Bundle
import android.widget.*
import android.view.View
//import android.widget.Toast
//import android.widget.Button
import android.content.Intent
import android.text.TextUtils
import android.view.ViewGroup
//import android.widget.TextView
import android.view.LayoutInflater
//import android.widget.EditText
import com.example.terravive.R
import androidx.core.view.ViewCompat
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class Register : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPass: EditText
    private lateinit var textViewLogin: TextView
    private lateinit var buttonReg: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        // Initialize UI elements
        editTextEmail = findViewById(R.id.register_email_input)
        editTextPassword = findViewById(R.id.register_password_input)
        editTextConfirmPass = findViewById(R.id.confirm_password_input)
        buttonReg = findViewById(R.id.register_button)
        textViewLogin = findViewById(R.id.login_here)

        textViewLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonReg.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPass.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("RegisterActivity", "createUserWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
            // You can navigate to home screen or login here:
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}