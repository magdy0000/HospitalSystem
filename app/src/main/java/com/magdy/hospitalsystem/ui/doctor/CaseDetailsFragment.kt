package com.magdy.hospitalsystem.ui.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.*
import com.magdy.hospitalsystem.databinding.FragmentCaseDetailsBinding
import com.magdy.hospitalsystem.network.NetworkState

import com.magdy.hospitalsystem.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CaseDetailsFragment : BaseFragment() {


    private var _binding: FragmentCaseDetailsBinding? = null
    private val binding get() = _binding!!

    private var nurseName = ""
    private val doctorViewModel: DoctorViewModel by viewModels()
    private var caseId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_case_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCaseDetailsBinding.bind(view)

        binding.textView4.setOnClickListener {

        }
        caseId = CaseDetailsFragmentArgs.fromBundle(requireArguments()).caseId

        doctorViewModel.showCase(caseId)
        doctorViewModel.showMedicalRecordDoctor(caseId)
        onClicks()
        observers()
        initView()
    }

    private fun initView() {
        binding.apply {
            if (MySharedPreferences.getUserType() == Const.MANAGER) {
                layoutCaseDetails.btnAddNurse.visibilityGone()
                layoutCaseDetails.btnRequest.visibilityGone()
            }

        }
    }

    private fun observers() {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("doctorId")
            ?.observe(
                viewLifecycleOwner
            ) { id ->

                if (id != 0 ) {

                    doctorViewModel.addNurse(caseId, id)
                    findNavController().currentBackStackEntry?.savedStateHandle?.set("doctorId", 0)


                }
            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("doctorName")
            ?.observe(
                viewLifecycleOwner
            ) { result ->

                nurseName = result

            }
        doctorViewModel.showCaseLiveData.observe(this, {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelCaseDetails


                    binding.layoutMedicalMeasurement.apply {
                        data.data.apply {
                            textDate.text = created_at
                            textDetails.text = measurement_note
                            textBloodPressure.text = blood_pressure
                            textSuger.text = sugar_analysis
                            textUserName.text = nurse_id
                        }

                    }


                    binding.layoutCaseDetails.apply {
                        data.data.apply {

                            textPatientAge.text = age + " " + getString(R.string.years)
                            textPatientDate.text = created_at
                            textPatientPhone.text = phone
                            textPatientName.text = patient_name
                            textPatientDesc.text = description
                            textPatientStatus.text = status
                            textPatientNurse.text = nurse_id
                            textPatientDoctor.text = doctor_id


                            if (status == Const.STATUS_LOGOUT) {
                                imageCondition.setImageResource(R.drawable.ic_check)
                            } else {
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

        doctorViewModel.showMedicalRecordDoctor.observe(this, {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelShowMedicalRecordDoctor

                    binding.layoutMedicalRecord.apply {
                        data.data.apply {

                            textDetails.text = note

                            Glide.with(myContext!!).load(image).into(imageMedialRecord)
                            textUserName.text = user.first_name + " " + user.last_name
                        }

                    }
                    ProgressLoading.dismiss()

                }
                else -> {
                    ProgressLoading.dismiss()

                }
            }
        })

        doctorViewModel.addNurseLiveData.observe(this, {

            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelCreation
                    showToast(data.message)
                    binding.layoutCaseDetails.textPatientNurse.text = nurseName
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
            layoutCaseDetails.btnAddNurse.setOnClickListener {
                navigate(
                    CaseDetailsFragmentDirections.actionCaseDetailsFragment2ToSelectDoctorForCallsFragment(
                        Const.NURSE
                    )
                )
            }

            layoutCaseDetails.btnRequest.setOnClickListener {
                navigate(
                    CaseDetailsFragmentDirections.actionCaseDetailsFragment2ToRequestAnalysisFragment(
                        caseId
                    )
                )
            }
            btnCase.setOnClickListener {
                btnMedicalRecord.background =
                    ContextCompat.getDrawable(myContext!!, R.drawable.rounded_gray_strock)
                binding.apply {
                    layoutCaseDetails.layoutCaseDetails.visibilityVisible()
                    layoutMedicalMeasurement.parentLayoutMedicalMeasurement.visibilityGone()
                    layoutMedicalRecord.parentMedicalRecordLayout.visibilityGone()
                }
                btnMedicalMeasurement.background =
                    ContextCompat.getDrawable(myContext!!, R.drawable.rounded_gray_strock)
                btnCase.setBackgroundColor(ContextCompat.getColor(myContext!!, R.color.main_color))

            }
            btnMedicalMeasurement.setOnClickListener {
                btnCase.background =
                    ContextCompat.getDrawable(myContext!!, R.drawable.rounded_gray_strock)

                binding.apply {
                    layoutCaseDetails.layoutCaseDetails.visibilityGone()
                    layoutMedicalMeasurement.parentLayoutMedicalMeasurement.visibilityVisible()
                    layoutMedicalRecord.parentMedicalRecordLayout.visibilityGone()
                }
                btnMedicalRecord.background =
                    ContextCompat.getDrawable(myContext!!, R.drawable.rounded_gray_strock)
                btnMedicalMeasurement.setBackgroundColor(
                    ContextCompat.getColor(
                        myContext!!,
                        R.color.main_color
                    )
                )

            }
            btnMedicalRecord.setOnClickListener {
                btnCase.background =
                    ContextCompat.getDrawable(myContext!!, R.drawable.rounded_gray_strock)
                binding.apply {
                    layoutCaseDetails.layoutCaseDetails.visibilityGone()
                    layoutMedicalMeasurement.parentLayoutMedicalMeasurement.visibilityGone()
                    layoutMedicalRecord.parentMedicalRecordLayout.visibilityVisible()
                }
                btnMedicalMeasurement.background =
                    ContextCompat.getDrawable(myContext!!, R.drawable.rounded_gray_strock)
                btnMedicalRecord.setBackgroundColor(
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