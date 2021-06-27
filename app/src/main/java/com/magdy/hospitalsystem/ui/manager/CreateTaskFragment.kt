package com.magdy.hospitalsystem.ui.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerCreateTask
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelAllUser
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.data.UsersData
import com.magdy.hospitalsystem.databinding.FragmentCreateTaskBinding
import com.magdy.hospitalsystem.databinding.FragmentHomeManagerBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateTaskFragment : BaseFragment() , NotifyAddTask {

    private var _binding  : FragmentCreateTaskBinding?= null
    private val binding get() = _binding!!
    private val todoList = ArrayList<String>()
    private var userId = 0
    private var userName = ""
    private val adapterRecyclerCreateTask :
            AdapterRecyclerCreateTask by lazy { AdapterRecyclerCreateTask() }
    private val managerViewModel  : ManagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_task , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateTaskBinding.bind(view)

        onClicks()
        observers()
    }

    private fun onClicks() {

        adapterRecyclerCreateTask.onTaskDeleted = object : AdapterRecyclerCreateTask.OnTaskDeleted{
            override fun onDelete(id: Int) {
                todoList.removeAt(id)

                adapterRecyclerCreateTask.list = todoList
                binding.recyclerTasks.adapter = adapterRecyclerCreateTask
            }

        }

        binding.apply {
            editDoctor.setOnClickListener {

                navigate(CreateTaskFragmentDirections.actionCreateTaskFragmentToSelectEmployeeFragment())
            }
            btnAdd.setOnClickListener {
                val popupTask = AddTaskPopup.newInstance()
                popupTask.notifyAddTask = this@CreateTaskFragment
                popupTask.show(childFragmentManager , "task")
            }
            btnSendTask.setOnClickListener {
                val taskName = editTaskName.text.toString().trim()
                val taskDescription  =editReportDescription.text.toString()
                if (taskName.isEmpty()){
                    editTaskName.error = getString(R.string.required)
                }else if (userId == 0){
                    showToast(getString(R.string.select_doctor_warn))

                }else if (taskDescription.isEmpty()){
                    editReportDescription.error = getString(R.string.required)
                }else if (todoList.size == 0){
                    showToast(getString(R.string.add_tasks_warn))
                }else{
                    managerViewModel.createTask(userId,taskName,taskDescription,todoList)
                }
            }
        }

    }

    private fun observers() {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("doctorId")
            ?.observe(
                viewLifecycleOwner
            ) { id ->

                userId = id

            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("doctorName")
            ?.observe(
                viewLifecycleOwner) { result ->


                binding.editDoctor.text =  result
            }

        managerViewModel.createTaskLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelCreation

                    findNavController().popBackStack()
                    ProgressLoading.dismiss()

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

    override fun taskCallBack(task: String) {
        todoList.add(task)
        adapterRecyclerCreateTask.list = todoList
        binding.recyclerTasks.adapter = adapterRecyclerCreateTask
        showToast(getString(R.string.added))
    }
}