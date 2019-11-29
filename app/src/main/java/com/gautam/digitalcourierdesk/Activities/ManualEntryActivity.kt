package com.gautam.digitalcourierdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_manual_entry.*
import kotlinx.android.synthetic.main.activity_manual_entry.nameText
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*

class ManualEntryActivity : AppCompatActivity() {
    private var name=""
    private var otp=0
    private var sn:Int?=0
    private var trackId:String?=""
    private var from:String?=""
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setDef()
        submitBtn.setOnClickListener {
            name=nameText.editText?.text.toString().toUpperCase().trim()
            trackId=trackID.editText?.text.toString().trim()
            from=senderText.editText?.text.toString().toUpperCase().trim()
            if (name.isNullOrBlank() || trackId.isNullOrBlank() || from.isNullOrBlank())
                toast("Invalid Entries")
            else{
                snTextView.text = ""
                nameText.isEnabled=false
                submitBtn.isEnabled=false
                trackID.isEnabled=false
                senderText.isEnabled=false
                longToast("Saving Entry")
                val r=Random()
                otp=r.nextInt(999999-100000)+100000
                db.collection("sn").document("sn").get().addOnSuccessListener {
                    sn=it.toObject(SN::class.java)?.sn
                    val values= hashMapOf(
                        "sn" to sn,
                        "delivered" to false,
                        "otp" to otp,
                        "date" to getDate(),
                        "sender" to from,
                    "Track ID" to trackId
                    )
                    db.collection("users").document(name).collection("parcels").document("$sn").set(values).addOnCompleteListener {
                        toast("Entry Successful")
                        sendEmail(sn,otp,name, from!!)
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

    private fun sendEmail(
        sn: Int?,
        otp: Int,
        name: String,
        from: String
    ) {
        db.collection("users").document(name).get().addOnSuccessListener {
            val email=it.toObject(Creds::class.java)?.Email
            if (email.isNullOrBlank()){
                toast("No Email Address Found")
            }
            else{
                val jm=JavaMailAPI(this,
                        email,
                        "New Parcel Recieved",
                        "Dear $name, \nYou've received a new parcel in your name from $from. The Serial Number is $sn and your OTP is $otp. \nThank you"
                )
                jm.execute()
            }
        }
            .addOnFailureListener{
                toast("Error Fetching Details")
            }

    }

    private fun setDef() {
        val intent= intent
        nameText.editText?.setText(intent.getStringExtra("name"))
        senderText.editText?.setText(intent.getStringExtra("sender"))
        trackID.editText?.setText(intent.getStringExtra("track"))
    }

    private fun updateSN(sn:Int?) {
        if (sn != null) {
            db.collection("sn").document("sn").set(hashMapOf("sn" to sn+1)).addOnSuccessListener {
                nameText.isEnabled=true
                submitBtn.isEnabled=true
                trackID.isEnabled=true
                senderText.isEnabled=true
                nameText.editText?.setText("")
                trackID.editText?.setText("")
                senderText.editText?.setText("")
            }
        }
    }

    private fun getDate(): String{
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return sdf.format(Date())
    }
}
