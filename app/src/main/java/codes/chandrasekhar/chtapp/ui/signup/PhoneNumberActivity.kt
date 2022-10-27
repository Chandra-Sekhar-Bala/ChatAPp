package codes.chandrasekhar.chtapp.ui.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import codes.chandrasekhar.chtapp.databinding.ActivityPhoneNumberBinding
import codes.chandrasekhar.chtapp.ui.signup.OTP.OTPActivity


class PhoneNumberActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPhoneNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()

        // Setting  CCP wih edittext
        binding.ccp.registerCarrierNumberEditText(binding.phoneNumber)
        // verify the user input then move on
        binding.btnContinue.setOnClickListener{

            if(verifyInput()){
                goToNext()
            }else{
                Toast.makeText(this@PhoneNumberActivity, "Please enter a valid number", Toast.LENGTH_SHORT).show()
            }
        }

    }
    // check the number field
    private fun verifyInput(): Boolean {
        return (binding.phoneNumber.text.toString().trim().isNotEmpty() &&
                binding.ccp.isValidFullNumber)
    }

    // Pass the phone number to next and move on
    private fun goToNext(){
        val intent = Intent(this@PhoneNumberActivity, OTPActivity::class.java)
        val number = binding.ccp.fullNumberWithPlus
        intent.putExtra("PHONE_NUMBER", number)
        startActivity(intent)
    }
}