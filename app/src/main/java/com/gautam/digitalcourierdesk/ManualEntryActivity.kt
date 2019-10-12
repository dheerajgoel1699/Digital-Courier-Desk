package com.gautam.digitalcourierdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_manual_entry.*
import org.jetbrains.anko.toast
import java.util.Random
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ManualEntryActivity : AppCompatActivity() {
    var name=""
    var otp=0
    var sn=0
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)
        submitBtn.setOnClickListener {
            name=nameText.editText?.text.toString().toUpperCase().trim()
            if (name.isNullOrBlank())
                toast("Invalid Name")
            else{
                val r=Random()
                otp=r.nextInt(9999999-100000)+100000
//                val a:Int=db.collection("sn").document("sn").get()
                val values= hashMapOf(
                    "sn" to sn,
                    "otp" to otp
                )
                db.collection("users").document(name).collection("parcels").document("$sn").set(values).addOnCompleteListener {
                    toast("Entry Successful")
                }.addOnCanceledListener {
                    toast("Error Adding")
                }
            }
        }





    }
}
