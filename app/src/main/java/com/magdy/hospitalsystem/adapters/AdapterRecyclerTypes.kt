package com.magdy.hospitalsystem.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.magdy.hospitalsystem.R

class AdapterRecyclerTypes : RecyclerView.Adapter<AdapterRecyclerTypes.Holder>() {


    var list : ArrayList<String> ?= null
    var onTapClick : OnTapClick ?= null
    var rowIndex  = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_types_tap, parent , false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)

        holder.apply {

            if (rowIndex == position){

                textType.background =ContextCompat.getDrawable(myContext!!
                                         ,R.drawable.background_rounded_selected)
                textType.setTextColor(ContextCompat.getColor(myContext!!,R.color.white))

                textType.text = data
            }else{
                textType.background =ContextCompat.getDrawable(myContext!!
                    ,R.drawable.rounded_gray_strock)
                textType.setTextColor(ContextCompat.getColor(myContext!!,R.color.black))

                textType.text = data

            }


        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }



    inner class Holder (view : View) : RecyclerView.ViewHolder(view){

        val textType = view.findViewById<TextView>(R.id.text_type)
        var myContext :Context ?= null
        init {
            myContext = view.context
            itemView.setOnClickListener {

                rowIndex = layoutPosition
                onTapClick?.onClick(list?.get(layoutPosition)!!)


                notifyDataSetChanged()
            }
        }

    }

    interface OnTapClick {
        fun onClick (type  :String)
    }
}