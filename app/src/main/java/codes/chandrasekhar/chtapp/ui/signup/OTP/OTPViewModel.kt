package codes.chandrasekhar.chtapp.ui.signup.OTP

import android.app.Activity
import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

enum class TimeOut { LOADING, }

class OTPViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    private val context = getApplication<Application>().applicationContext
    private var verificationID: String? = null

    private var _timeOut = MutableLiveData<Boolean>()
    val timeOut: LiveData<Boolean> get() = _timeOut

    private var _time = MutableLiveData<Long>()
    val time: LiveData<Long> get() = _time

    var progress = MutableLiveData<Boolean>()

    private val timer = object : CountDownTimer(30000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            if (_timeOut.value == false)
                _timeOut.value = true

            _time.value = millisUntilFinished / 1000
        }

        override fun onFinish() {
            _timeOut.value = false
        }
    }

    fun sendOTP(number: String, activity: Activity) {
        Log.d("TAG", "send OTP func ${_timeOut.value}")
        // If timer already stared then go out else initiate sending OTP and start timer
        if (timeOut.value == true) return
        timer.start()
        progress.value = true
        Log.d("TAG", "initiating bro")
        viewModelScope.launch(Dispatchers.IO) {
            val provider = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number)
                .setActivity(activity)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        signInWithPhoneAuthCredential(credential)
                        progress.value = false
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        progress.value = false
                        Toast.makeText(activity, p0.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(
                        verifyID: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verifyID, p1)
                        verificationID = verifyID
                        progress.value = false
                        Log.d("TAG", "verify iD: $verifyID")
                        Toast.makeText(activity, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
                    }
                }).build()
            PhoneAuthProvider.verifyPhoneNumber(provider)
        }
    }

    // Make a sign-in Credential and try to sign-in
    fun verifyOTP(otp: String) {
        progress.value = true
        Log.d("TAG", "otp $otp")
        val credential = verificationID?.let { PhoneAuthProvider.getCredential(it, otp) }
        if (credential != null) {
            signInWithPhoneAuthCredential(credential)
        }
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Log.d("TAG", "verifying")
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                progress.value = false
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
            progress.value = false
            Log.d("TAG", "Failed " + it.message.toString())
            Toast.makeText(
                context,
                it.message.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}