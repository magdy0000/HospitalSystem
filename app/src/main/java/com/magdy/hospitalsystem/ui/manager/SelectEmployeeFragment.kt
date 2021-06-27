package com.magdy.hospitalsystem.ui.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerEmployee
import com.magdy.hospitalsystem.adapters.AdapterRecyclerSelectDoctor
import com.magdy.hospitalsystem.adapters.AdapterRecyclerTypes
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelAllUser
import com.magdy.hospitalsystem.data.UsersData
import com.magdy.hospitalsystem.databinding.FragmentEmployeeBinding
import com.magdy.hospitalsystem.databinding.FragmentSelectEmployeeBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.ui.hr.EmployeeFragmentDirections
import com.magdy.hospitalsystem.ui.hr.HrViewModel
import com.magdy.hospitalsystem.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectEmployeeFragment:  BaseFragment() {


    private var _binding : FragmentSelectEmployeeBinding?= null
    private val binding get() = _binding!!
    private val hrViewModel : HrViewModel by viewModels()
    private var type = "All"
    private val typesList = ArrayList<String>()
    private val adapterRecyclerTypes : AdapterRecyclerTypes
              by lazy { AdapterRecyclerTypes() }

    private var doctorId = 0
    private var doctorName = ""
    private val adapterRecyclerSelectDoctor : AdapterRecyclerSelectDoctor
            by lazy { AdapterRecyclerSelectDoctor() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_employee , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSelectEmployeeBinding.bind(view)

        hrViewModel.getAllUsers(type,"")
        initView()
        onClicks()
        observers()
    }
    private fun initView() {

        binding.apply {
            typesList.add("All")
            typesList.add(Const.DOCTOR)
            typesList.add(Const.NURSE)
            typesList.add(Const.ANALYSIS)
            typesList.add(Const.RECEPTIONIST)
            typesList.add(Const.MANAGER)
            typesList.add(Const.HR)


            adapterRecyclerTypes.list = typesList
            recyclerTypesTaps.adapter = adapterRecyclerTypes



        }
    }

    private fun onClicks() {

        adapterRecyclerTypes.onTapClick = object  :AdapterRecyclerTypes.OnTapClick{
            override fun onClick(type: String) {
                this@SelectEmployeeFragment.type = type
                hrViewModel.getAllUsers(this@SelectEmployeeFragment.type , binding.editSearch.text.toString())
            }
        }

        adapterRecyclerSelectDoctor.onUserClick = object  : AdapterRecyclerSelectDoctor.OnUserClick{
            override fun onClick(id: Int, name: String) {
                doctorId = id
                doctorName = name
            }

        }

        binding.apply {


            btnSelectEmployee.setOnClickListener {
                if (doctorId ==0 ){
                    showToast(getString(R.string.select_doctor_warn))
                    return@setOnClickListener
                }
                findNavController().previousBackStackEntry?.savedStateHandle?.set("doctorId", doctorId)
                findNavController().previousBackStackEntry?.savedStateHandle?.set("doctorName", doctorName)
                findNavController().popBackStack()

            }

            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }
            btnSearch.setOnClickListener {
                hrViewModel.getAllUsers(Const.DOCTOR,editSearch.text.toString())
            }
        }
    }

    private fun observers (){
        hrViewModel.getAllUsersLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelAllUser

                    adapterRecyclerSelectDoctor.list = data.data as ArrayList<UsersData>
                    binding.recyclerEmployee.adapter = adapterRecyclerSelectDoctor
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
        _binding= null
    }

}