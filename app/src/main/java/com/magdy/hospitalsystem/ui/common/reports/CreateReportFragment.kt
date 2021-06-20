package com.magdy.hospitalsystem.ui.common.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelAllReports
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.data.ReportsData
import com.magdy.hospitalsystem.databinding.FragmentCreateReportBinding
import com.magdy.hospitalsystem.databinding.FragmentReceptionHomeBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateReportFragment : BaseFragment() {

    private var _binding  : FragmentCreateReportBinding?= null
    private val binding get() = _binding!!
    private val reportsViewModel  : ReportsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_report , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateReportBinding.bind(view)

        observers()
        onClicks()
    }

    private fun observers (){
        reportsViewModel.createReportLiveData.observe(this ,{
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

    private fun onClicks (){
        binding.apply {
            btnCreateReport.setOnClickListener {
                validation()
            }
            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }
        }
    }

    private fun validation (){
        val reportTittle = binding.editReportTittle.text.toString()
        val reportDescription = binding.editReportDescription.text.toString()

        if (reportTittle.isEmpty()){
            binding.editReportTittle.error = getString(R.string.required)
        }else if (reportDescription.isEmpty()){
            binding.editReportDescription.error = getString(R.string.required)
        }else{
            reportsViewModel.createReport(reportTittle,reportDescription)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}