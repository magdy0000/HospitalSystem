package com.magdy.hospitalsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.data.UsersData

class AdapterRecyclerSelectDoctor : RecyclerView.Adapter<AdapterRecyclerSelectDoctor.Holder>() {


    var list : ArrayList<UsersData> ?= null
    var onUserClick : OnUserClick ?= null

     var rowIndex  = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_select_doctor, parent , false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)

        holder.apply {

            holder.textName.text = data?.first_name
            holder.textType.text = data?.type

            if (rowIndex == position){
                holder.imageSelect.setImageResource(R.drawable.ic_radio_button_selected)
            }else{
                holder.imageSelect.setImageResource(R.drawable.ic_radio_un_selected)
            }
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder (view : View) : RecyclerView.ViewHolder(view){

        val textType = view.findViewById<TextView>(R.id.text_type)
        val textName = view.findViewById<TextView>(R.id.text_user_name)
        val imageSelect = view.findViewById<ImageView>(R.id.image_select)

        init {
            itemView.setOnClickListener {
                rowIndex = layoutPosition
                onUserClick?.onClick(list?.get(layoutPosition)?.id!!
                                     ,list?.get(layoutPosition)?.first_name!! )
                notifyDataSetChanged()
            }
        }

    }

    interface OnUserClick {
        fun onClick (id : Int , name :String)
    }
}