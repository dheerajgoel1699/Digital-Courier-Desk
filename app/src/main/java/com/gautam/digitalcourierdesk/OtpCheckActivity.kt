package com.gautam.digitalcourierdesk

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_otp_check.*
import kotlinx.android.synthetic.main.otp_view_layout.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast


class OtpCheckActivity : AppCompatActivity() {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_check)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nameSub.setOnClickListener {
            val name=rcnText.editText?.text.toString().trim().toUpperCase()
            val sn=snText.editText?.text.toString().trim()
            if (name.isNullOrBlank() || sn.isNullOrBlank())
                toast("Invalid Values")
            else{
                toast("Fetching Parcels")
                fetchParcels(name,Integer.parseInt(sn))
            }}

    }

    private fun fetchParcels(name: String, sn: Int) {
        val ref=
        db.collection("users").document(name).collection("parcels").whereEqualTo("sn",sn).get()
            .addOnSuccessListener {
                for (document in it) {
                    enableOtp(document, name,sn)
                }
            }.addOnFailureListener{
                longToast("Error $it")
            }
    }

    private fun enableOtp(
        document: QueryDocumentSnapshot,
        name: String,
        sn: Int
    ) {
//        val builder=AlertDialog.Builder(this)
        val alert=Dialog(this)
        alert.setContentView(R.layout.otp_view_layout)
        alert.create()
        alert.setCancelable(false)
        alert.show()
        alert.sender.text="nihgghg"
        alert.sender.text = document["sender"].toString()
        alert.trID.text=document["Track ID"].toString()
        val otp=document["otp"].toString()
        alert.checkBtn.setOnClickListener {
            val otpn=alert.otpText.text.toString().trim()
            if (otpn.isNullOrBlank())
                toast("Invalid OTP")
            else if (otpn == otp){
                toast("Marking Delivered")
                var ref = db.collection("users").document(name).collection("parcels").document(sn.toString()).delete()
                    .addOnSuccessListener {
                        alert.dismiss()
                    }
            }
            else{
                toast("Invalid OTP")
            }
        }
    }
}