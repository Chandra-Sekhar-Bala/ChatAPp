package codes.chandrasekhar.chtapp.ui.signup.OTP

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import codes.chandrasekhar.chtapp.R
import codes.chandrasekhar.chtapp.databinding.ActivityOtpverificationBinding

class OTPActivity : AppCompatActivity() {

    private lateinit var viewModel: OTPViewModel
    private lateinit var binding: ActivityOtpverificationBinding
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

        // set-up viewModel
        val number = intent.getStringExtra("PHONE_NUMBER")
        binding.showNumber.text = "Sent to $number"
        viewModel.sendOTP(number!!, this)

        viewModel.time.observe(this) { sec ->
            binding.timer.text = "in $sec s"
        }
        viewModel.timeOut.observe(this) { timeOut ->
            Log.d("TAGATAG", "callback $timeOut")
            if (timeOut == true) {
                binding.resendOtp.setTextColor(resources.getColor(R.color.grey))
                binding.resendOtp.isActivated = false
            } else {
                binding.resendOtp.setTextColor(resources.getColor(R.color.blue))
                binding.resendOtp.isActivated = true
            }
        }
        viewModel.progress.observe(this) { value->
            binding.progressBar.visibility = when(value){
                true -> View.VISIBLE
                false -> View.GONE
            }
        }

        binding.btnVerify.setOnClickListener {
            if (binding.pinView.text.toString().trim().length == 6) {
                verifyOTP(binding.pinView.text.toString())
            } else {
                Toast.makeText(this, "Please fill the OTP", Toast.LENGTH_SHORT).show()
            }
        }
        binding.resendOtp.setOnClickListener {
            viewModel.sendOTP(number, this)
        }

    }

    private fun verifyOTP(number: String) {
        viewModel.verifyOTP(number)
    }
}