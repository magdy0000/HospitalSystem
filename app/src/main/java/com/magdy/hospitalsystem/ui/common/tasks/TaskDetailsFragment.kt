package com.magdy.hospitalsystem.ui.common.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerToDo
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelCreation

import com.magdy.hospitalsystem.data.ModelTaskDetails
import com.magdy.hospitalsystem.data.ToDo

import com.magdy.hospitalsystem.databinding.FragmentTasksDetailsBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDetailsFragment  : BaseFragment() {


    private var _binding  : FragmentTasksDetailsBinding?= null
    private val binding get() = _binding!!
    private val adapterRecyclerToDo
                : AdapterRecyclerToDo by lazy { AdapterRecyclerToDo() }
    private val taskViewModel : TasksViewModel by viewModels()

    private var taskId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks_details , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTasksDetailsBinding.bind(view)
        taskId = TaskDetailsFragmentArgs.fromBundle(requireArguments()).taskId

        taskViewModel.showTask(taskId)
        observers()
        onClicks()
        initView()

    }
    private fun initView () {
        binding.apply {
            if (MySharedPreferences.getUserType() == Const.MANAGER) {
                btnExecution.visibilityGone()
                editNoteTask.visibilityGone()
            }
        }
    }
    private fun onClicks (){

        binding.apply {

            btnExecution.setOnClickListener {
                taskViewModel.execution(taskId,  editNoteTask.text.toString())
            }

                btnBack.setOnClickListener {
                    myActivity?.onBackPressed()
                }

        }

    }
    private fun observers (){

        taskViewModel.showTaskLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelTaskDetails

                    binding.apply {
                        data.data.apply {

                            textTaskName.text = task_name
                            textDate.text = created_at
                            textUserName.text = user.first_name+" "+user.last_name
                            textType.text = "Specialist - ${user.specialist}"
                            if (MySharedPreferences.getUserType() == Const.MANAGER){
                                textDetails.text = note
                            }else{
                                textDetails.text = description
                            }


                            adapterRecyclerToDo.list = to_do as ArrayList<ToDo>
                            recyclerTodo.adapter = adapterRecyclerToDo
                        }
                    }
                    ProgressLoading.dismiss()

                }
                else -> {
                    ProgressLoading.dismiss()
                    showToast(it.msg)
                }
            }
        })

        taskViewModel.executionLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelCreation

                    showToast(data.message)
                    ProgressLoading.dismiss()
                    findNavController().popBackStack()

                }
                else -> {
                    ProgressLoading.dismiss()
                    showToast(it.msg)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}