package com.gautam.digitalcourierdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

val db by lazy{
    FirebaseFirestore.getInstance()
}

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
        resetSerial.setOnClickListener {
            toast("Resetting Serial Number")
            resetSN()
        }
    }

    private fun resetSN() {
        db.collection("sn").document("sn").set(hashMapOf("sn" to 1)).addOnSuccessListener {
            toast("Reset Successful")
        }
    }
}