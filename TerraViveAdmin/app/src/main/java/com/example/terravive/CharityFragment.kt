package com.example.terravive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.content.Intent
import android.widget.EditText
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.Button

class CharityFragment : Fragment() {

    //    Spinners for Donation and Recipient
    private lateinit var spinnerDonationType: Spinner
    private lateinit var spinnerRecipient: Spinner
    private lateinit var spinnerPaymentMethod: Spinner
    private lateinit var cbTerms: CheckBox
    private lateinit var tvTerms: TextView
    private lateinit var donorInputEditText: EditText
    private lateinit var donationInputEditText: EditText
    private lateinit var btnProceed: Button
    private lateinit var donationTextView: TextView
    private lateinit var paymentMethodTextView: TextView
    private lateinit var otpInputEditText: EditText // Added for OTP input

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment.
        return inflater.inflate(R.layout.fragment_charity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerDonationType = view.findViewById(R.id.spinnerDonationType)
        spinnerRecipient = view.findViewById(R.id.spinnerRecipient)
        spinnerPaymentMethod = view.findViewById(R.id.spinnerPaymentMethod)
        cbTerms = view.findViewById(R.id.cbTerms)
        tvTerms = view.findViewById(R.id.tvTerms)
        donorInputEditText = view.findViewById(R.id.donor_input)
        donationInputEditText = view.findViewById(R.id.donation_input)
        btnProceed = view.findViewById(R.id.btnProceed)
        donationTextView = view.findViewById(R.id.donationTextView)
        paymentMethodTextView = view.findViewById(R.id.paymentMethodTextView)
        otpInputEditText = view.findViewById(R.id.otpInputEditText) // Find the OTP EditText

        donationInputEditText.visibility = View.GONE
        spinnerPaymentMethod.visibility = View.GONE
        donationTextView.visibility = View.GONE
        paymentMethodTextView.visibility = View.GONE

        // Set up the adapter for the donation type spinner
        val donationTypes = arrayOf("Select Type of Donation", "Money", "Food", "Clothing", "Medicine", "Toys", "Others")
        val donationTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_item,
            donationTypes
        )
        spinnerDonationType.adapter = donationTypeAdapter

        // Set up the adapter for the payment method spinner
        val payMethod = arrayOf("Select Payment Method", "GCash", "PayMaya", "GoTyme", "Bank Transfer", "PayPal")
        val payMethodAdapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_item,
            payMethod
        )
        spinnerPaymentMethod.adapter = payMethodAdapter

        // Set up the adapter for the recipient spinner
        val recipients = arrayOf(
            "Select Recipient",
            "SLU Sunflower Child and Youth Wellness Center",
            "Save Our School Children Foundation Incorporation",
            "IGOROTA Foundation, Inc.",
            "Helping Hands, Healing Hearts Ministries Philippines, Inc",
            "Shepherd of the Hills Children's Foundation (SOTH)"
        )
        val recipientAdapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_item,
            recipients
        )
        spinnerRecipient.adapter = recipientAdapter

        setupTermsAndConditionsTextView()

        // Set listener for donation type spinner
        spinnerDonationType.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (donationTypes[position] == "Money") {
                    donationInputEditText.visibility = View.VISIBLE
                    spinnerPaymentMethod.visibility = View.VISIBLE
                    donationTextView.visibility = View.VISIBLE
                    paymentMethodTextView.visibility = View.VISIBLE
                } else {
                    donationInputEditText.visibility = View.GONE
                    spinnerPaymentMethod.visibility = View.GONE
                    donationTextView.visibility = View.GONE
                    paymentMethodTextView.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                donationInputEditText.visibility = View.GONE
                spinnerPaymentMethod.visibility = View.GONE
                donationTextView.visibility = View.GONE
                paymentMethodTextView.visibility = View.GONE
            }
        })

        btnProceed.setOnClickListener {
            validateInputsAndShowDialog()
        }
    }

    private fun setupTermsAndConditionsTextView() {
        val fullText = getString(R.string.terms_and_conditions)
        val spannableString = SpannableString(fullText)

        val termsStart = fullText.indexOf("Terms and Conditions")
        val termsEnd = fullText.length

        val iHaveReadStart = 0
        val iHaveReadEnd = fullText.indexOf("Terms and Conditions")

        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.brown)),
            iHaveReadStart,
            iHaveReadEnd,
            0
        )

        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.light_green)),
            termsStart,
            termsEnd,
            0
        )
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(requireContext(), TermsAndConditionsActivity::class.java)
                startActivity(intent)
            }
        }
        spannableString.setSpan(clickableSpan, termsStart, termsEnd, 0)

        tvTerms.text = spannableString
        tvTerms.movementMethod = android.text.method.LinkMovementMethod.getInstance()
        cbTerms.text = ""
    }

    private fun validateInputsAndShowDialog() {
        val donorName = donorInputEditText.text.toString().trim()
        val donationType = spinnerDonationType.selectedItem.toString()
        val recipient = spinnerRecipient.selectedItem.toString()
        val donationAmount = donationInputEditText.text.toString().trim()
        val paymentMethod = spinnerPaymentMethod.selectedItem?.toString() ?: ""
        val isTermsChecked = cbTerms.isChecked

        var errorMessage = ""

        if (donorName.isEmpty()) {
            errorMessage = "Please fill all fields."
        } else if (donationType == "Select Type of Donation") {
            errorMessage = "Please fill all fields."
        } else if (recipient == "Select Recipient") {
            errorMessage = "Please fill all fields."
        } else if (donationType == "Money" && donationAmount.isEmpty()) {
            errorMessage = "Please fill all fields."
        } else if (donationType == "Money" && paymentMethod == "Select Payment Method") {
            errorMessage = "Please fill all fields."
        } else if (!isTermsChecked) {
            errorMessage = "Please fill all fields."
        }

        if (errorMessage.isNotEmpty()) {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            return
        }

        // Show dialog based on donation type
        if (donationType == "Money") {
            showVerificationDialog(donorName, donationType, recipient, donationAmount, paymentMethod)
        } else {
            showDonationConfirmedDialog(donorName, donationType, recipient, donationAmount)
        }
    }

    private fun showVerificationDialog(
        donorName: String,
        donationType: String,
        recipient: String,
        donationAmount: String,
        paymentMethod: String
    ) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.item_otp, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        // Make the background of the fragment blurred
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)

        val confirmButton = dialogView.findViewById<Button>(R.id.confirmButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val otpEditText = dialogView.findViewById<EditText>(R.id.otpInputEditText) // Find it here

        confirmButton.setOnClickListener {
            val otp = otpEditText.text.toString() // Get the OTP
            if (otp.length == 6) {
                dialog.dismiss()
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                showDonationConfirmedDialog(donorName, donationType, recipient, donationAmount) // Show item_donation_confirm after successful OTP
            } else {
                Toast.makeText(requireContext(), "Please enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        }
    }

    private fun showDonationConfirmedDialog(
        donorName: String,
        donationType: String,
        recipient: String,
        donationAmount: String
    ) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.item_donation_confirm, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val okButton = dialogView.findViewById<Button>(R.id.backButton)

        okButton.setOnClickListener {
            dialog.dismiss()
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            // Clear the input fields
            donorInputEditText.text.clear()
            donationInputEditText.text.clear()
            spinnerDonationType.setSelection(0)
            spinnerRecipient.setSelection(0)
            spinnerPaymentMethod.setSelection(0)
            cbTerms.isChecked = false
        }
    }
}
