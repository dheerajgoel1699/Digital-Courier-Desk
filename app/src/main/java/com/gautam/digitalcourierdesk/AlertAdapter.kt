package com.gautam.digitalcourierdesk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ParcelAdapter(val context: Context, val parcels: ArrayList<String> ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater= LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.alerts_layout,parent,false)
        return MoviesViewsHolder(view)
    }

    override fun getItemCount(): Int {
        return parcels.size
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
class MoviesViewsHolder(itemView : View) : RecyclerView.ViewHolder()
