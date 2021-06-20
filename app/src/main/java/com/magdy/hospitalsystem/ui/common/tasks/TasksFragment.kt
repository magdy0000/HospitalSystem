package com.magdy.hospitalsystem.ui.common.tasks

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerTasks
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelAllTasks
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.data.TasksData
import com.magdy.hospitalsystem.databinding.FragmentReceptionHomeBinding
import com.magdy.hospitalsystem.databinding.TasksFragmentBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.ui.reception.CreateCallFragmentDirections
import com.magdy.hospitalsystem.ui.reception.ReceptionViewModel
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class TasksFragment : BaseFragment() {


    private val adapterRecyclerTasks: AdapterRecyclerTasks
            by lazy { AdapterRecyclerTasks() }

    private var _binding: TasksFragmentBinding? = null
    private val binding get() = _binding!!
    private val cal: Calendar = Calendar.getInstance()
    private var startDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val fullFormat = SimpleDateFormat("yyyy-MM-dd")

    private val tasksViewModel: TasksViewModel by viewModels()
    private var date = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tasks_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = TasksFragmentBinding.bind(view)

        tasksViewModel.showAllTasks(date)


        observers()
        onClicks()
    }

    private fun observers() {

        tasksViewModel.showAllTasksLiveData.observe(this, {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelAllTasks

                    adapterRecyclerTasks.list = data.data as ArrayList<TasksData>
                    binding.recyclerTasks.adapter = adapterRecyclerTasks
                    ProgressLoading.dismiss()
                }
                else -> {
                    ProgressLoading.dismiss()
                    showToast(it.msg)
                }
            }
        })
    }

    private fun onClicks() {
        binding.textDate.text = fullFormat.format(cal.time)

        adapterRecyclerTasks.onUserClick = object : AdapterRecyclerTasks.OnTasksClick {
            override fun onClick(id: Int) {

                navigate(TasksFragmentDirections.actionTasksFragmentToTaskDetailsFragment(id))
            }
        }

        startDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                val realMonth = month + 1
                val myMonth = if (realMonth < 10) "0$realMonth" else realMonth.toString()
                val myday = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()

                date = "${year}-${myMonth}-${myday}"
                binding.textDate.text = date
                tasksViewModel.showAllTasks(date)
            }

        binding.apply {
            imageView5.setOnClickListener {
                datePicker()
            }
            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }
        }
    }

    private fun datePicker() {
        val dialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            startDateSetListener,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}