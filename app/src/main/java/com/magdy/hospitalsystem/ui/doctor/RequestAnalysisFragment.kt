package com.magdy.hospitalsystem.ui.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerAnalysis
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.CasesData
import com.magdy.hospitalsystem.data.ModelAllCases
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.databinding.FragmentRequestAnalysisBinding
import com.magdy.hospitalsystem.databinding.LoginFragmentBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.ui.auth.LoginViewModel
import com.magdy.hospitalsystem.utils.Const
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RequestAnalysisFragment : BaseFragment() {


    private var _binding  : FragmentRequestAnalysisBinding?= null
    private val binding get() = _binding!!
    private var analystId = 0
    private var caseId=  0
    private val adapterRecyclerAnalysis : AdapterRecyclerAnalysis
                    by lazy { AdapterRecyclerAnalysis() }
    private val analysisList   = ArrayList<String>()
    private val doctorViewModel  : DoctorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_request_analysis , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRequestAnalysisBinding.bind(view)
        adapterRecyclerAnalysis.list = analysisList
        binding.recyclerMedicalRecord.adapter = adapterRecyclerAnalysis
        caseId =  RequestAnalysisFragmentArgs.fromBundle(requireArguments()).caseId
        onClicks()
        observers()
    }

    private fun observers() {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("doctorId")
            ?.observe(
                viewLifecycleOwner
            ) { id ->
               analystId = id
            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("doctorName")
            ?.observe(
                viewLifecycleOwner) { result ->
                binding.editDoctor.text = result
            }
        doctorViewModel.requestAnalysisLiveData.observe(this ,{
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

    private fun onClicks() {

        adapterRecyclerAnalysis.onRemoveClick = object : AdapterRecyclerAnalysis.OnRemoveClick{
            override fun onClick(name: String) {

                analysisList.remove(name)
                adapterRecyclerAnalysis.list = analysisList
                binding.recyclerMedicalRecord.adapter = adapterRecyclerAnalysis
            }
        }
        binding.apply {
            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }
            btnSend.setOnClickListener {
                if (analysisList.size == 0){
                    showToast(getString(R.string.analysis_warn))
                    return@setOnClickListener
                }else if (analystId == 0){
                    showToast(getString(R.string.analysis_emp_warn))
                    return@setOnClickListener
                }
                doctorViewModel.requestAnalysis(caseId, analystId, binding.editNoteTask.text.toString(),analysisList)
            }
            editDoctor.setOnClickListener {
                navigate(RequestAnalysisFragmentDirections.actionRequestAnalysisFragmentToSelectDoctorForCallsFragment(Const.ANALYSIS))
            }

            btnAdd.setOnClickListener {
                val analysis = binding.editMeasurement.text.toString()
                if (analysis == ""){
                    binding.editMeasurement.error = getString(R.string.required)
                    return@setOnClickListener
                }

                analysisList.add(analysis)
                adapterRecyclerAnalysis.list = analysisList
                binding.recyclerMedicalRecord.adapter = adapterRecyclerAnalysis
                binding.editMeasurement.setText("")


            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}