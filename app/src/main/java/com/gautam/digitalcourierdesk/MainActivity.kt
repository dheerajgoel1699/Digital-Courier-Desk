package com.gautam.digitalcourierdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifyOtp.setOnClickListener {
            startActivity<OtpCheckActivity>()
        }
        scanParcel.setOnClickListener {
            startActivity<CameraActivity>()
        }
        pastRecords.setOnClickListener {
            startActivity<PastAlertsActivity>()
        }
    }
}