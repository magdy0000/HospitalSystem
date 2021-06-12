package com.magdy.hospitalsystem.ui.reception

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelAllUser
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.data.UsersData
import com.magdy.hospitalsystem.databinding.FragmentCreateCallBinding
import com.magdy.hospitalsystem.databinding.FragmentReceptionCallsBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCallFragment : BaseFragment() {

    private var _binding  : FragmentCreateCallBinding?= null
    private val binding get() = _binding!!

    private val receptionViewModel : ReceptionViewModel by viewModels()

    private var doctorId  = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_call , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateCallBinding.bind(view)

        onClicks()
        observers()
    }
    private fun onClicks (){

        binding.apply {
            editDoctor.setOnClickListener {
                navigate(CreateCallFragmentDirections.actionCreateCallFragmentToSelectDoctorForCallsFragment())
            }

            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }
            btnCreateCall.setOnClickListener {
                validation()
            }
        }

    }

    private fun validation() {
        val patientName = binding.editPatientName.text.toString()
        val patientAge = binding.editPatientAge.text.toString()
        val patientPhone = binding.editPatientPhone.text.toString()
        val patientDescription = binding.editCaseDescription.text.toString()

        if (patientName.isEmpty()){
            binding.editPatientName.error = getString(R.string.required)
        }else if (patientAge.isEmpty()){
            binding.editPatientAge.error = getString(R.string.required)
        }else if (patientPhone.isEmpty()){
            binding.editPatientPhone.error = getString(R.string.required)
        }else if (doctorId  == 0){
            showToast(getString(R.string.select_doctor_warn))
        }else if (patientDescription.isEmpty()){
            binding.editCaseDescription.error = getString(R.string.required)
        }else{
            receptionViewModel.createCall(patientName,patientAge,doctorId,patientPhone,patientDescription)
        }

    }

    private fun observers(){
        findNavController().currentBackStackEntry?.
        savedStateHandle?.getLiveData<Int>("doctorId")?.observe(
            viewLifecycleOwner) { result ->
            doctorId = result
        }
        findNavController().currentBackStackEntry?.
        savedStateHandle?.getLiveData<String>("doctorName")?.observe(
            viewLifecycleOwner) { result ->
           binding.editDoctor.text = result
        }

        receptionViewModel.createCallLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelCreation

                    showToast(data.message)
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


}