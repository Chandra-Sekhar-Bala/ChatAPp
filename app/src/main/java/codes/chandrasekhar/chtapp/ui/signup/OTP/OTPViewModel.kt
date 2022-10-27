package codes.chandrasekhar.chtapp.ui.signup.OTP

import android.app.Activity
import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class OTPViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    private val context = getApplication<Application>().applicationContext
    private lateinit var verificationID: String

    private var timeOut = false

    private val timer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timeOut = true
        }

        override fun onFinish() {
            timeOut = false
        }
    }

    fun sendOTP(number: String, activity: Activity) {
        // If timer already stared then go out else initiate sending OTP and start timer
        if (timeOut) return
        timer.start()

        viewModelScope.launch(Dispatchers.IO) {
            val provider = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number)
                .setActivity(activity)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        signInWithPhoneAuthCredential(credential)
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        TODO("Not yet implemented")
                    }

                    override fun onCodeSent(
                        verifyID: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verifyID, p1)
                        verificationID = verifyID
                    }
                }).build()
            PhoneAuthProvider.verifyPhoneNumber(provider)
        }
    }

    // Make a sign-in Credential and try to sign-in
    fun verifyOTP(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationID, otp)
        signInWithPhoneAuthCredential(credential)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("TAG", "success")
                Toast.makeText(
                    context,
                    "Success",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d("TAG", it.exception?.message ?: "Error on verifying")
                Toast.makeText(
                    context,
                    "Please check then verification code",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener {
            Log.d("TAG", "Failed "+ it.message.toString())
            Toast.makeText(
                context,
                it.message.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}