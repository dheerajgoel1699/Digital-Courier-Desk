package com.gautam.digitalcourierdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_otp_check.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast


class OtpCheckActivity : AppCompatActivity() {
    val db by lazy {
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
        db.collection("users").document(name).collection("parcels").whereEqualTo("sn",sn).get()
            .addOnSuccessListener {
                for (document in it) {
                    val a=document.data["otp"]
                    Log.i("workk",a.toString())
                    Log.i("workk", "${document.id} => ${document.data}")
                }
            }.addOnFailureListener{
                longToast("Error $it")
            }
    }
}