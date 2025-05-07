package com.example.terravive
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.terravive.R
import com.google.firebase.firestore.FirebaseFirestore

class OrganizerCreateFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var etLocation: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnDelete: Button  // New button for deletion

    private var activityId: String? = null  // Store the activity ID for later deletion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_organizer_create, container, false)

        etName = view.findViewById(R.id.etActivityName)
        etDate = view.findViewById(R.id.etActivityDate)
        etTime = view.findViewById(R.id.etActivityTime)
        etLocation = view.findViewById(R.id.etActivityLocation)
        btnSubmit = view.findViewById(R.id.btnSubmitActivity)
        btnDelete = view.findViewById(R.id.btnDeleteActivity)

        btnSubmit.setOnClickListener {
            val activityName = etName.text.toString()
            val activityDate = etDate.text.toString()
            val activityTime = etTime.text.toString()
            val activityLocation = etLocation.text.toString()

            // Validate input fields
            if (activityName.isNotEmpty() && activityDate.isNotEmpty() && activityTime.isNotEmpty() && activityLocation.isNotEmpty()) {
                submitActivity(activityName, activityDate, activityTime, activityLocation)
            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle delete button click
        btnDelete.setOnClickListener {
            activityId?.let { id ->
                deleteActivity(id)
            } ?: run {
                Toast.makeText(context, "No activity to delete", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun submitActivity(name: String, date: String, time: String, location: String) {
        val activity = hashMapOf(
            "name" to name,
            "date" to date,
            "time" to time,
            "location" to location,
            "status" to "pending"  // Default status is "pending"
        )

        // Save the activity to Firestore
        FirebaseFirestore.getInstance()
            .collection("activities_pending")
            .add(activity)
            .addOnSuccessListener { documentReference ->
                // Save the document ID for later deletion
                activityId = documentReference.id
                Toast.makeText(context, "Activity submitted for approval", Toast.LENGTH_SHORT).show()

                // Show the delete button after submission
                btnDelete.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteActivity(activityId: String) {
        FirebaseFirestore.getInstance()
            .collection("activities_pending")
            .document(activityId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Activity deleted successfully", Toast.LENGTH_SHORT).show()

                // Clear input fields and hide the delete button after deletion
                clearInputFields()
                btnDelete.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearInputFields() {
        // Clear input fields after activity is submitted or deleted
        etName.text.clear()
        etDate.text.clear()
        etTime.text.clear()
        etLocation.text.clear()
    }
}
