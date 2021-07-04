package com.magdy.hospitalsystem.ui.nurse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelCaseDetails
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.databinding.FragmentNurseCaseDetailsBinding
import com.magdy.hospitalsystem.databinding.LoginFragmentBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.ui.auth.LoginViewModel
import com.magdy.hospitalsystem.ui.doctor.CaseDetailsFragmentArgs
import com.magdy.hospitalsystem.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NurseCaseDetailsFragment : BaseFragment() {

    private var _binding: FragmentNurseCaseDetailsBinding? = null
    private val binding get() = _binding!!

    private var doctorId : Int ?= null
    private var patientName : String ?= null
    private var caseId = 0
    val nurseViewModel: NurseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_nurse_case_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNurseCaseDetailsBinding.bind(view)

        caseId = NurseCaseDetailsFragmentArgs.fromBundle(requireArguments()).caseId

        nurseViewModel.showCase(caseId)
        onClicks()
        observers()

    }

    private fun observers() {
        nurseViewModel.showCaseLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelCaseDetails

                   doctorId =  data.data.doc_id
                    patientName = data.data.patient_name

                    binding.layoutMedicalMeasurement.apply {
                        data.data.apply {
                            textDate.text=created_at
                            textDetails.text = measurement_note
                            textUserName.text = doctor_id
                        }

                    }
                    binding.layoutCaseDetailsNurse.apply {
                        data.data.apply {

                            textPatientAge.text = age +" "+ getString(R.string.years)
                            textPatientDate.text = created_at
                            textPatientPhone.text = phone
                            textPatientName.text = patient_name
                            textPatientDesc.text=  description
                            textPatientStatus.text= status
                            textPatientNurse.text = nurse_id
                            textPatientDoctor.text = doctor_id


                            if (status== Const.STATUS_LOGOUT){
                                imageCondition.setImageResource(R.drawable.ic_check)
                            }else{
                                imageCondition.setImageResource(R.drawable.ic_delay)

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
        nurseViewModel.uploadMeasurementLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelCreation

                    showToast(data.message)
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
            layoutCaseDetailsNurse.btnCallDoctor.setOnClickListener {

                if (doctorId == null)
                    return@setOnClickListener

                nurseViewModel.sendNotification(doctorId!! ,"Emergency"
                    , "come to patient $patientName")
            }


            layoutMedicalMeasurement.btnAddMeasurement.setOnClickListener {
                val suger = layoutMedicalMeasurement.editSuger.text.toString()
                val bloodPressure = layoutMedicalMeasurement.editBloodPressure.text.toString()
                val heartRate = layoutMedicalMeasurement.editHeartRate.text.toString()
                val fluidBalance = layoutMedicalMeasurement.editFluidBalance.text.toString()
                val respiratoryRate = layoutMedicalMeasurement.editRespiratoryRate.text.toString()
                val temp = layoutMedicalMeasurement.editTemp.text.toString()
                val note= layoutMedicalMeasurement.editNoteMeasurement.text.toString()
                if (bloodPressure.isEmpty()){
                    layoutMedicalMeasurement.editBloodPressure.error = getString(R.string.required)
                }else if (suger.isEmpty()){
                    layoutMedicalMeasurement.editSuger.error = getString(R.string.required)

                }else if (temp.isEmpty()){
                    layoutMedicalMeasurement.temp.error = getString(R.string.required)

                }else if (fluidBalance.isEmpty()){
                    layoutMedicalMeasurement.editFluidBalance.error = getString(R.string.required)

                }else if (respiratoryRate.isEmpty()){
                    layoutMedicalMeasurement.editRespiratoryRate.error = getString(R.string.required)

                }else if (heartRate.isEmpty()){
                    layoutMedicalMeasurement.editHeartRate.error = getString(R.string.required)

                }else{
                    nurseViewModel.uploadMeasurement(caseId,bloodPressure
                           ,suger,temp,fluidBalance,respiratoryRate
                        ,heartRate,note)
                }
            }

            btnCase.setOnClickListener {
                binding.apply {
                    layoutCaseDetailsNurse.parentCaseDetailsNurse.visibilityVisible()
                    layoutMedicalMeasurement.parentLayoutMedicalMeasurementNurse.visibilityGone()

                }
                btnMedicalMeasurement.background =
                    ContextCompat.getDrawable(myContext!!, R.drawable.rounded_gray_strock)
                btnCase.setBackgroundColor(ContextCompat.getColor(myContext!!, R.color.main_color))

            }
            btnMedicalMeasurement.setOnClickListener {
                btnCase.background =
                    ContextCompat.getDrawable(myContext!!, R.drawable.rounded_gray_strock)

                binding.apply {
                    layoutCaseDetailsNurse.parentCaseDetailsNurse.visibilityGone()
                    layoutMedicalMeasurement.parentLayoutMedicalMeasurementNurse.visibilityVisible()

                }
                btnMedicalMeasurement.setBackgroundColor(
                    ContextCompat.getColor(
                        myContext!!,
                        R.color.main_color
                    )
                )

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}