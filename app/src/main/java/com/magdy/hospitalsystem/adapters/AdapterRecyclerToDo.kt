package com.magdy.hospitalsystem.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.data.ModelTaskDetails
import com.magdy.hospitalsystem.data.TaskDetails
import com.magdy.hospitalsystem.data.ToDo

class AdapterRecyclerToDo : RecyclerView.Adapter<AdapterRecyclerToDo.Holder>() {

    var list : ArrayList<ToDo> ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_to_do, parent , false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)

        holder.apply {
            textTittle.text = data?.title
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class Holder (view : View) : RecyclerView.ViewHolder(view){

        val textTittle = view.findViewById<TextView>(R.id.text_tittle)



    }


}