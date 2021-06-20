package com.magdy.hospitalsystem.ui.reception

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.data.ModelShowCall
import com.magdy.hospitalsystem.databinding.FragmentCaseFragmentBinding
import com.magdy.hospitalsystem.databinding.FragmentReceptionHomeBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CaseDetailsFragment : BaseFragment() {

    private var _binding  : FragmentCaseFragmentBinding?= null
    private val binding get() = _binding!!
    var caseId = 0
    private val receptionViewModel : ReceptionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_case_fragment , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCaseFragmentBinding.bind(view)

         caseId = CaseDetailsFragmentArgs.fromBundle(requireArguments()).callId

        receptionViewModel.showCall(caseId)
        observers()
        onClicks()

    }
    private fun observers (){

        receptionViewModel.showCallLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelShowCall

                    binding.apply {
                        data.data.apply {

                            textPatientAge.text = age +" "+ getString(R.string.years)
                            textPatientDate.text = created_at
                            textPatientPhone.text = phone
                            textPatientName.text = patient_name
                            textPatientDescription.text=  description
                            textPatientStatus.text= status

                            if (status== Const.STATUS_LOGOUT){
                                imageCondition.setImageResource(R.drawable.ic_check)
                                binding.btnLogout.visibilityGone()
                            }else{
                                imageCondition.setImageResource(R.drawable.ic_delay)
                                binding.btnLogout.visibilityVisible()
                            }
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

        receptionViewModel.logoutCallLiveData.observe(this ,{
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

    private fun onClicks (){
        binding.btnLogout.setOnClickListener {
            receptionViewModel.logoutCall(caseId)
        }

        binding.btnBack.setOnClickListener {
            myActivity?.onBackPressed()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}