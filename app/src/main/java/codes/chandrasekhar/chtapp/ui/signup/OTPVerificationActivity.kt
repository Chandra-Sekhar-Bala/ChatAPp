package codes.chandrasekhar.chtapp.ui.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import codes.chandrasekhar.chtapp.R
import codes.chandrasekhar.chtapp.databinding.ActivityOtpverificationBinding

class OTPVerificationActivity : AppCompatActivity() {
    lateinit var binding : ActivityOtpverificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpverification)
        setContentView(binding.root)
        // hide action bar lol
        supportActionBar?.hide()

        binding.pinView.requestFocus()

    }

    override fun onResume() {
        super.onResume()
        val number = intent.getStringExtra("PHONE_NUMBER")
        binding.showNumber.text = "Sent to $number"
        // send OTP ASAP
        sendOTP()
    }

    private fun sendOTP() {

    }
}