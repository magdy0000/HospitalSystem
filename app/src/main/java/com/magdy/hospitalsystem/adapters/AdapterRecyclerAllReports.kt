package com.magdy.hospitalsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.data.CallsData
import com.magdy.hospitalsystem.data.ModelAllReports
import com.magdy.hospitalsystem.data.ReportsData
import com.magdy.hospitalsystem.utils.Const

class AdapterRecyclerAllReports : RecyclerView.Adapter<AdapterRecyclerAllReports.Holder>() {


    var list : ArrayList<ReportsData> ?= null
    var onReportClick : OnReportClick ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reception_call, parent , false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)

        holder.apply {

            holder.textName.text = data?.report_name
            holder.textDate.text = data?.created_at


            if (data?.status == Const.STATUS_LOGOUT){
                holder.imageCondition.setImageResource(R.drawable.ic_done)
            }else{
                holder.imageCondition.setImageResource(R.drawable.ic_delay)
            }
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder (view : View) : RecyclerView.ViewHolder(view){

        val textName = view.findViewById<TextView>(R.id.text_name)
        val textDate = view.findViewById<TextView>(R.id.text_date)
        val imageCondition = view.findViewById<ImageView>(R.id.ic_condation)

        init {
            itemView.setOnClickListener {
                onReportClick?.onClick(list?.get(layoutPosition)?.id!!)
            }
        }

    }

    interface OnReportClick {
        fun onClick (id : Int)
    }
}