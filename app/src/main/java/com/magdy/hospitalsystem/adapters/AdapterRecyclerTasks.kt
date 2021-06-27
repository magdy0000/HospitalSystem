package com.magdy.hospitalsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.data.CallsData
import com.magdy.hospitalsystem.data.ModelAllTasks
import com.magdy.hospitalsystem.data.TasksData
import com.magdy.hospitalsystem.utils.Const
import com.magdy.hospitalsystem.utils.MySharedPreferences

class AdapterRecyclerTasks : RecyclerView.Adapter<AdapterRecyclerTasks.Holder>() {


    var list : ArrayList<TasksData> ?= null
    var onUserClick : OnTasksClick ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reception_call, parent , false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list?.get(position)

        holder.apply {

            holder.textName.text = data?.task_name
            holder.textDate.text = data?.created_at


            if (data?.status == Const.STATS_TASK_DONE){
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
                if (list?.get(layoutPosition)?.status !=Const.STATS_TASK_DONE
                    || MySharedPreferences.getUserType() == Const.MANAGER) {
                    onUserClick?.onClick(list?.get(layoutPosition)?.id!!)
                }
            }
        }

    }

    interface OnTasksClick {
        fun onClick (id : Int)
    }
}