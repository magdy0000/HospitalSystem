package com.magdy.hospitalsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.data.ToDo

class AdapterRecyclerCreateTask :  RecyclerView.Adapter<AdapterRecyclerCreateTask.Holder>() {

    var list: ArrayList<String>? = null

    var onTaskDeleted  :OnTaskDeleted?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_create_task, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)

        holder.apply {
            textTittle.text = data
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

        val textTittle = view.findViewById<TextView>(R.id.text_todo)
        val btnDeleteTask = view.findViewById<ImageView>(R.id.btn_delete_task)

        init {
            btnDeleteTask.setOnClickListener {
                onTaskDeleted?.onDelete(layoutPosition)
            }
        }
    }
    interface OnTaskDeleted{

        fun onDelete (id : Int)

    }

}
