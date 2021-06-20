package com.magdy.hospitalsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.data.CasesData

class AdapterRecyclerAnalysis  : RecyclerView.Adapter<AdapterRecyclerAnalysis.Holder>() {


    var list : ArrayList<String> ?= null
    var onRemoveClick : OnRemoveClick ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_analysis, parent , false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)

        holder.apply {

            holder.textName.text = data
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder (view : View) : RecyclerView.ViewHolder(view){

        val textName = view.findViewById<TextView>(R.id.text_analysis)
        val btnRemove = view.findViewById<ImageView>(R.id.btn_remove)


        init {
            btnRemove.setOnClickListener {
                onRemoveClick?.onClick(list?.get(layoutPosition)!!)
            }

        }

    }

    interface OnRemoveClick {
        fun onClick (name : String)

    }
}