package com.example.terravive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.text.Html
import android.os.Build
import android.view.View

class TermsAndConditionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.terms_and_conditions)

        val termsTextView: TextView = findViewById(R.id.termsAndConditionsTextView)

        val termsAndConditionsText = getString(R.string.terms_and_conditions)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            termsTextView.setText(Html.fromHtml(termsAndConditionsText, Html.FROM_HTML_MODE_LEGACY))
        } else {
            termsTextView.setText(Html.fromHtml(termsAndConditionsText))
        }
    }

    fun goBack(view: View) {
        finish()
    }
}
