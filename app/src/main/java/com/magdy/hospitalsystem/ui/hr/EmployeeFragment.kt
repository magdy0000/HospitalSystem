package com.magdy.hospitalsystem.ui.hr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerEmployee
import com.magdy.hospitalsystem.adapters.AdapterRecyclerTypes
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelAllUser
import com.magdy.hospitalsystem.data.ModelUser
import com.magdy.hospitalsystem.data.UsersData
import com.magdy.hospitalsystem.databinding.FragmentEmployeeBinding
import com.magdy.hospitalsystem.databinding.FragmentHomeHrBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.Const
import com.magdy.hospitalsystem.utils.MySharedPreferences
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployeeFragment  :BaseFragment() {

    private var _binding : FragmentEmployeeBinding?= null
    private val binding get() = _binding!!
    private val typesList = ArrayList<String>()
    private val adapterRecyclerTypes :AdapterRecyclerTypes by lazy { AdapterRecyclerTypes() }
    private val hrViewModel : HrViewModel by viewModels()
    private var type = "All"
    private val adapterRecyclerEmployee : AdapterRecyclerEmployee by lazy { AdapterRecyclerEmployee() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_employee , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEmployeeBinding.bind(view)

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

            btnSearch.setOnClickListener {
                hrViewModel.getAllUsers(type  , editSearch.text.toString())
            }

            btnAddUser.setOnClickListener {
                navigate(EmployeeFragmentDirections.actionEmployeeFragmentToRegisterNewEmployeeFragment())
            }
            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }
        }
    }

    private fun onClicks() {

        adapterRecyclerTypes.onTapClick = object  :AdapterRecyclerTypes.OnTapClick{
            override fun onClick(type: String) {
               this@EmployeeFragment.type = type
                hrViewModel.getAllUsers(this@EmployeeFragment.type , binding.editSearch.text.toString())
            }
        }

        adapterRecyclerEmployee.onUserClick = object  : AdapterRecyclerEmployee.OnUserClick{
            override fun onClick(id: Int) {
                navigate(EmployeeFragmentDirections.actionEmployeeFragmentToProfileFragment(false,id))
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

                    adapterRecyclerEmployee.list = data.data as ArrayList<UsersData>
                    binding.recyclerEmployee.adapter = adapterRecyclerEmployee
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


}