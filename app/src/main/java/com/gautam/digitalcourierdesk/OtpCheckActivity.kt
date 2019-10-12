package com.gautam.digitalcourierdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_otp_check.*

class OtpCheckActivity : AppCompatActivity() {
private var otp:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_check)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nameCheckLoad.hide()



//        otp=Integer.parseInt(otpText.editText?.text.toString())





    }
}
