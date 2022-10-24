package codes.chandrasekhar.chtapp.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import codes.chandrasekhar.chtapp.R

class PhoneNumberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        supportActionBar?.hide()
    }
}