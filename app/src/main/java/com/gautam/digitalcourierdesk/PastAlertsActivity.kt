package com.gautam.digitalcourierdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_past_alerts.*
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.customView

class PastAlertsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_alerts)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Snackbar.make(rootLayout,"Lemme See if this shit work",Snackbar.LENGTH_INDEFINITE)
            .setAction("Okay") {
            }.show()
        pastAlertView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        pastAlertView.adapter=AlertAdapter()
    }
}
