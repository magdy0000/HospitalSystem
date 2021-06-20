package com.magdy.hospitalsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.data.CallsData
import com.magdy.hospitalsystem.utils.Const

class AdapterRecyclerCallsDoctor : RecyclerView.Adapter<AdapterRecyclerCallsDoctor.Holder>() {


    var list : ArrayList<CallsData> ?= null
    var onUserClick : OnUserClick ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor_calls, parent , false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)

        holder.apply {

            holder.textName.text = data?.patient_name
            holder.textDate.text = data?.created_at



        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder (view : View) : RecyclerView.ViewHolder(view){

        val textName = view.findViewById<TextView>(R.id.text_name)
        val textDate = view.findViewById<TextView>(R.id.text_date)
        val btnAccept = view.findViewById<MaterialButton>(R.id.btn_accept)
        val btnReject = view.findViewById<MaterialButton>(R.id.btn_reject)

        init {
            btnAccept.setOnClickListener {
                onUserClick?.onAccept(list?.get(layoutPosition)?.id!!)
            }
            btnReject.setOnClickListener {
                onUserClick?.onReject(list?.get(layoutPosition)?.id!!)
            }
        }

    }

    interface OnUserClick {
        fun onAccept (id : Int)
        fun onReject (id : Int)
    }
}