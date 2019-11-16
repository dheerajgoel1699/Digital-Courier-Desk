package com.gautam.digitalcourierdesk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.parcels_layout.view.*

class ParcelAdapter(private val context: Context, private val parcels: ArrayList<String> ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val db by lazy {
        FirebaseFirestore.getInstance()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater= LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.parcels_layout,parent,false)
        return MoviesViewsHolder(view)
    }

    override fun getItemCount(): Int {
        return parcels.size
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val name=parcels[position]
        holder.itemView.apply {
            nameText.text=name
        }
    } }
class MoviesViewsHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
