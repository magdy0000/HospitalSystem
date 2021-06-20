package com.magdy.hospitalsystem.ui.common.calls_cases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerSelectDoctor
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelAllUser
import com.magdy.hospitalsystem.data.UsersData
import com.magdy.hospitalsystem.databinding.FragmentSelectDoctorForCallsBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.ui.hr.HrViewModel
import com.magdy.hospitalsystem.utils.Const
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectDoctorForCallsFragment : BaseFragment() {

    private var _binding  : FragmentSelectDoctorForCallsBinding?= null
    private val binding get() = _binding!!


    private val adapterRecyclerSelectDoctor : AdapterRecyclerSelectDoctor
                                     by lazy { AdapterRecyclerSelectDoctor() }

    private var doctorId = 0
    private var doctorName = ""
    private val hrViewModel : HrViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_doctor_for_calls , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSelectDoctorForCallsBinding.bind(view)

        val searchKey = SelectDoctorForCallsFragmentArgs.fromBundle(requireArguments()).searchKey
        onClicks()
        observers()

        hrViewModel.getAllUsers(searchKey,"")
    }

    private fun observers() {
        hrViewModel.getAllUsersLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelAllUser

                    adapterRecyclerSelectDoctor.rowIndex = -1
                    adapterRecyclerSelectDoctor.list = data.data as ArrayList<UsersData>
                    binding.recyclerDoctors.adapter = adapterRecyclerSelectDoctor
                    ProgressLoading.dismiss()

                }
                else -> {
                    ProgressLoading.dismiss()
                    showToast(it.msg)
                }
            }
        })
    }

    private fun onClicks (){

        adapterRecyclerSelectDoctor.onUserClick = object : AdapterRecyclerSelectDoctor.OnUserClick{
            override fun onClick(id: Int  , name :String) {
                doctorId = id
                doctorName = name
            }

        }

        binding.apply {
            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }

            btnSelectDoctor.setOnClickListener {
                if (doctorId ==0 ){
                    showToast(getString(R.string.select_doctor_warn))
                    return@setOnClickListener
                }
                findNavController().previousBackStackEntry?.savedStateHandle?.set("doctorId", doctorId)
                findNavController().previousBackStackEntry?.savedStateHandle?.set("doctorName", doctorName)
                findNavController().popBackStack()

            }
            btnSearch.setOnClickListener {
                hrViewModel.getAllUsers(Const.DOCTOR,editSearch.text.toString())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}