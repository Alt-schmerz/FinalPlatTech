import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.text.Html
import android.os.Build
import com.example.terravive.R

class TermsAndConditionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.terms_and_conditions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val termsTextView: TextView = view.findViewById(R.id.termsAndConditionsTextView)


        val termsAndConditionsText = """
            <b>Welcome to TerraVive</b><br><br>
            A platform created to empower individuals, foundations, and organizations to support and promote environmental initiatives. By creating an account, you agree to the following Terms and Conditions. Please review them carefully.<br><br>
            <b>1. Acceptance of Terms</b><br><br>
            By registering for and using an account on TerraVive, you agree to comply with these Terms and Conditions, as well as our Privacy Policy. If you do not agree with any part of these terms, please do not use the platform.<br><br>
            <b>2. Eligibility</b><br><br>
            To create an account on TerraVive, you must:<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Be at least 18 years old or the legal age of majority in your jurisdiction.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Provide truthful, current, and complete information during registration.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Not create an account on behalf of another person or organization without authorization.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Verified foundation and charity accounts must be registered with the appropriate legal authorities in their region.<br><br>
            <b>3. User Responsibilities</b><br><br>
            As a TerraVive user, you are responsible for:<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Keeping your login credentials secure and confidential.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• All activity conducted through your account.<br><br>
             &nbsp;&nbsp;&nbsp;&nbsp;• Ensuring your posts, campaigns, and interactions align with TerraVive’s community standards and environmental mission.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Misuse or unauthorized access must be reported to TerraVive immediately.<br><br>
            <b>4. Fundraising and Donation Features</b><br><br>
            TerraVive allows users to:<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Launch and manage fundraisers for environmental causes.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Collect and receive donations via integrations with trusted third-party financial apps.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Promote campaigns to raise awareness and support for green projects.<br><br>
            Important: All fundraisers undergo verification. Fraudulent or deceptive fundraising activities are strictly prohibited and will result in account suspension or legal action.<br><br>
            <b>5. Use of Donations</b><br><br>
            Users and organizations receiving funds agree to:<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Use all donations exclusively for the purposes stated in their fundraising campaign.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Maintain transparency and provide updates or documentation upon request.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Avoid using any portion of the funds for unauthorized or personal purposes.<br><br>
            <b>6. Community & Social Features</b><br><br>
            TerraVive includes a social media-like space where users can:<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Share updates, photos, stories, and achievements.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Comment on and support environmental initiatives.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Connect with other users, groups, and eco-conscious services.<br><br>
            Prohibited Content Includes:<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Harassment, hate speech, or threats.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Inappropriate or misleading content.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Advertisements unrelated to environmental causes.<br><br>
            TerraVive reserves the right to remove content and restrict user access if community guidelines are violated.<br><br>
            <b>7. Account Suspension and Termination</b><br><br>
            TerraVive may suspend or terminate an account if:<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• These Terms are violated.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Fraudulent or deceptive practices are discovered.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Donations are misused or not accounted for.<br><br>
            <b>8. Third-Party Services</b><br><br>
            TerraVive integrates with external financial services for donation collection. By using these features:<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• You acknowledge and accept the terms and privacy policies of those third-party providers.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• TerraVive is not liable for issues arising from their use.<br><br>
            <b>9. Limitation of Liability</b><br><br>
            TerraVive is not liable for:<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Disputes between donors and fundraisers.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Mismanagement of donations outside our platform.<br><br>
            &nbsp;&nbsp;&nbsp;&nbsp;• Indirect or consequential damages resulting from platform use.<br><br>
            <b>10. Modifications to Terms</b><br><br>
            These Terms may be updated from time to time. Continued use of TerraVive after changes indicates your acceptance of the new Terms.<br><br>
            <b>11. Contact Information</b><br><br>
            For questions, support, or concerns, please reach out to us at support@terravive.org.
            """.trimIndent()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            termsTextView.setText(Html.fromHtml(termsAndConditionsText, Html.FROM_HTML_MODE_LEGACY))
        } else {
            termsTextView.setText(Html.fromHtml(termsAndConditionsText))
        }
    }
}
