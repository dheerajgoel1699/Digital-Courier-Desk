package com.gautam.digitalcourierdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_otp_check.*

class OtpCheckActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_check)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        

    }
}
