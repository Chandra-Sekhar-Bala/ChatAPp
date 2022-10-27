package codes.chandrasekhar.chtapp.ui.signup.OTP

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import codes.chandrasekhar.chtapp.databinding.ActivityOtpverificationBinding

class OTPActivity : AppCompatActivity() {

    private lateinit var viewModel : OTPViewModel
    private lateinit var binding : ActivityOtpverificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.pinView.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        val factory = OTPViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[OTPViewModel::class.java]

        val number = intent.getStringExtra("PHONE_NUMBER")
        binding.showNumber.text = "Sent to $number"
        viewModel.sendOTP(number!!, this)

        binding.btnVerify.setOnClickListener{
            sendOTP(binding.pinView.text.toString())
        }
    }

    private fun sendOTP(number : String ) {
        viewModel.verifyOTP(number)
    }
}