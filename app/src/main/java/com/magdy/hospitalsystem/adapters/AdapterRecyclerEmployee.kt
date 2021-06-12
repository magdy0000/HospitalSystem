package com.magdy.hospitalsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.data.UsersData

class AdapterRecyclerEmployee : RecyclerView.Adapter<AdapterRecyclerEmployee.Holder>() {


    var list : ArrayList<UsersData> ?= null
    var onUserClick : OnUserClick ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       val view = LayoutInflater.from(parent.context)
               .inflate(R.layout.item_recycler_employee, parent , false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)

        holder.apply {

            holder.textName.text = data?.first_name
            holder.textType.text = data?.type
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder (view : View) : RecyclerView.ViewHolder(view){

        val textType = view.findViewById<TextView>(R.id.text_type);
        val textName = view.findViewById<TextView>(R.id.text_user_name);

        init {
            itemView.setOnClickListener {
                onUserClick?.onClick(list?.get(layoutPosition)?.id!!)
            }
        }

    }

    interface OnUserClick {
        fun onClick (id : Int)
    }
}