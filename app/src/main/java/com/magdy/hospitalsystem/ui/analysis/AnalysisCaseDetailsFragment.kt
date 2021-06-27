package com.magdy.hospitalsystem.ui.analysis

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerAnalysis
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelCaseDetails
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.data.ModelShowMedicalRecordAnalysis
import com.magdy.hospitalsystem.databinding.FragmentCaseDetailsAnalysisBinding
import com.magdy.hospitalsystem.databinding.LoginFragmentBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.ui.auth.LoginViewModel
import com.magdy.hospitalsystem.ui.nurse.NurseCaseDetailsFragmentArgs
import com.magdy.hospitalsystem.ui.nurse.NurseViewModel
import com.magdy.hospitalsystem.utils.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.security.AccessController.checkPermission


@AndroidEntryPoint
class AnalysisCaseDetailsFragment  : BaseFragment(){

    private var _binding  : FragmentCaseDetailsAnalysisBinding?= null
    private val binding get() = _binding!!
    private val adapterRecyclerAnalysis : AdapterRecyclerAnalysis by lazy { AdapterRecyclerAnalysis() }
    private var caseId  = 0
    private val analysisViewModel  : AnalysisViewModel by viewModels()
    private var images: List<Image>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_case_details_analysis , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCaseDetailsAnalysisBinding.bind(view)

        caseId = AnalysisCaseDetailsFragmentArgs.fromBundle(requireArguments()).caseId

        analysisViewModel.showCase(caseId)
        analysisViewModel.showMedicalRecordAnalysis(caseId)
        onClicks()
        observers()
    }

    private fun observers() {
        analysisViewModel.showCaseLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelCaseDetails


                    binding.layoutMedicalRecord.apply {
                        data.data.apply {
                            textDate.text=created_at
                            textDetails.text = measurement_note
                            textUserName.text = doctor_id
                        }

                    }
                    binding.layoutCaseDetailsAnalysis.apply {
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

        analysisViewModel.showMedicalRecordAnalysis.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelShowMedicalRecordAnalysis


                    binding.layoutMedicalRecord.apply {
                        data.data.apply {

                            textDetails.text = note
                            textUserName.text = user.first_name + " " +user.last_name
                            adapterRecyclerAnalysis.list = type as ArrayList<String>
                            recyclerAnalysis.adapter = adapterRecyclerAnalysis
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

        analysisViewModel.uploadRecordAnalysis.observe(this ,{
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

            layoutMedicalRecord.btnUploadImage.setOnClickListener {
                ImagePicker.create(this@AnalysisCaseDetailsFragment).start()
            }

            layoutMedicalRecord.btnAddRecord.setOnClickListener {
                if (images?.size != 0)
                prepareFilePart(images?.get(0)!!)
            }

        btnCase.setOnClickListener {
            binding.apply {
                layoutCaseDetailsAnalysis.parentCaseDetailsAnalysis.visibilityVisible()
                layoutMedicalRecord.parentCaseDetailsAnalysis.visibilityGone()

            }
            btnMedicalRecord.background =
                ContextCompat.getDrawable(myContext!!, R.drawable.rounded_gray_strock)
            btnCase.setBackgroundColor(ContextCompat.getColor(myContext!!, R.color.main_color))

        }
            btnMedicalRecord.setOnClickListener {
            btnCase.background =
                ContextCompat.getDrawable(myContext!!, R.drawable.rounded_gray_strock)

            binding.apply {
                layoutCaseDetailsAnalysis.parentCaseDetailsAnalysis.visibilityGone()
                layoutMedicalRecord.parentCaseDetailsAnalysis.visibilityVisible()

            }
                btnMedicalRecord.setBackgroundColor(
                ContextCompat.getColor(
                    myContext!!,
                    R.color.main_color
                )
            )

        }
    }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images

            images = ImagePicker.getImages(data)

            Glide.with(myContext!!).load(images?.get(0)?.path).into(binding.layoutMedicalRecord.uploadImage)

        }
    }
    fun prepareFilePart(image: Image) {


        val file = File(image.path)
        val bitmap = BitmapFactory.decodeFile(file.path)
    //    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, FileOutputStream(file))

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multiPartImage = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val caseId: RequestBody = caseId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val note: RequestBody = "Done".toRequestBody("text/plain".toMediaTypeOrNull())

        val status: RequestBody = "Done".toRequestBody("text/plain".toMediaTypeOrNull())

        ProgressLoading.show(requireActivity())
        analysisViewModel.uploadRecordAnalysis(multiPartImage, caseId , note , status)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}