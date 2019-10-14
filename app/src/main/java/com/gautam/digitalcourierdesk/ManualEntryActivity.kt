package com.gautam.digitalcourierdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_manual_entry.*
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*

class ManualEntryActivity : AppCompatActivity() {
    var name=""
    var otp=0
    private var sn:Int?=0
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        submitBtn.setOnClickListener {
            name=nameText.editText?.text.toString().toUpperCase().trim()
            if (name.isNullOrBlank())
                toast("Invalid Name")
            else{
                snTextView.text = ""
                nameText.isEnabled=false
                submitBtn.isEnabled=false
                longToast("Saving Entry")
                val r=Random()
                otp=r.nextInt(999999-100000)+100000
                db.collection("sn").document("sn").get().addOnSuccessListener {
                    sn=it.toObject(SN::class.java)?.sn
                    val values= hashMapOf(
                        "sn" to sn,
                        "delivered" to false,
                        "otp" to otp,
                        "date" to getDate()
                    )
                    db.collection("users").document(name).collection("parcels").document("$sn").set(values).addOnCompleteListener {
                        toast("Entry Successful")
                        runOnUiThread {
                            snTextView.text = "$sn"
                        }
                        updateSN(sn)
                    }.addOnCanceledListener {
                        toast("Error Adding")
                    }
                } } }
        getDate()
    }

    private fun updateSN(sn:Int?) {
        if (sn != null) {
            db.collection("sn").document("sn").set(hashMapOf("sn" to sn+1)).addOnSuccessListener {
                nameText.isEnabled=true
                submitBtn.isEnabled=true
            }
        }
    }

    private fun getDate(): String{
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return sdf.format(Date())
    }
}
