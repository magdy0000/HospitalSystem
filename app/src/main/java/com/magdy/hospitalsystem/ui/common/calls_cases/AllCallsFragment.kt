package com.magdy.hospitalsystem.ui.common.calls_cases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerCallsDoctor
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.CallsData
import com.magdy.hospitalsystem.data.ModelAllCalls
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.databinding.FragmentDoctorCallsBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.ui.doctor.DoctorViewModel
import com.magdy.hospitalsystem.utils.Const
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCallsFragment : BaseFragment() {

    private var _binding  : FragmentDoctorCallsBinding?= null
    private val binding get() = _binding!!
    private var status = ""
    private val viewModel  : GeneralCallsAndCasesViewModel by viewModels()
    private val adapterRecyclerCallsDoctor : AdapterRecyclerCallsDoctor by lazy { AdapterRecyclerCallsDoctor() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_calls , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorCallsBinding.bind(view)

        onClicks()
        observers()
        viewModel.getCalls()

    }
    private fun observers (){
        viewModel.getCallsLiveData.observe(this ,{

            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelAllCalls

                    adapterRecyclerCallsDoctor.list  = data.data as ArrayList<CallsData>
                    binding.recyclerDoctorCalls.adapter =adapterRecyclerCallsDoctor


                    ProgressLoading.dismiss()

                }
                else -> {
                    ProgressLoading.dismiss()
                    showToast(it.msg)
                }
            }
        })

        viewModel.acceptRejectCallLiveData.observe(this ,{

            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelCreation

                    showToast(data.message)
                    if (status == Const.ACCEPT_CALL){
                        navigate(AllCallsFragmentDirections.actionDoctorCallFragmentToDoctorAllCasesFragment())
                    }else {
                        findNavController().popBackStack()
                    }
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
        binding.apply {
            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }
        }

        adapterRecyclerCallsDoctor.onUserClick = object : AdapterRecyclerCallsDoctor.OnUserClick{
            override fun onAccept(id: Int) {

                status = Const.ACCEPT_CALL
                viewModel.acceptRejectCall(id , Const.ACCEPT_CALL)
            }

            override fun onReject(id: Int) {
                status = Const.REJECT_CALL
                viewModel.acceptRejectCall(id , Const.REJECT_CALL)
            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}