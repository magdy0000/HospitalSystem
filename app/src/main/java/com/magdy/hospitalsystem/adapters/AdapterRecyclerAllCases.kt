package com.magdy.hospitalsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.data.CallsData
import com.magdy.hospitalsystem.data.CasesData
import com.magdy.hospitalsystem.data.ModelAllCases

class AdapterRecyclerAllCases : RecyclerView.Adapter<AdapterRecyclerAllCases.Holder>() {


    var list : ArrayList<CasesData> ?= null
    var onShowClick : OnShowClick ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_all_cases, parent , false)

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
        val btnShowCase = view.findViewById<MaterialButton>(R.id.btn_show_case)


        init {
            btnShowCase.setOnClickListener {
                onShowClick?.show(list?.get(layoutPosition)?.id!!)
            }

        }

    }

    interface OnShowClick {
        fun show (id : Int)

    }
}