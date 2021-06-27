package com.magdy.hospitalsystem.ui.common.reports

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerAllReports
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelAllReports
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.data.ModelShowReport
import com.magdy.hospitalsystem.data.ReportsData
import com.magdy.hospitalsystem.databinding.FragmentReportsBinding
import com.magdy.hospitalsystem.databinding.FragmentShowReportBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ShowReportFragment : BaseFragment() {

    private var _binding  : FragmentShowReportBinding?= null
    private val binding get() = _binding!!

    private val reportsViewModel : ReportsViewModel by viewModels()

    var reportId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_report , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShowReportBinding.bind(view)


        observer()
        onClicks()
        hideMangerView()
         reportId = ShowReportFragmentArgs.fromBundle(requireArguments()).reportId

        reportsViewModel.showReport(reportId)

    }
    private fun hideMangerView(){
        binding.apply {
            if (MySharedPreferences.getUserType() == Const.MANAGER) {

                textDate2.visibilityGone()
                cardView2.visibilityGone()
                textType2.visibilityGone()
                textDetailsManager.visibilityGone()
                btnEndReport.visibilityGone()
            } else {
                editReplyManager.visibilityGone()
                btnSendReplyReport.visibilityGone()
            }
        }

    }

    private fun onClicks (){
        binding.apply {
            btnEndReport.setOnClickListener {
                reportsViewModel.endReports(reportId)
            }

            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }

            btnSendReplyReport.setOnClickListener {
                val answer  = editReplyManager.text.toString().trim()

                if (answer.isEmpty()) {
                    editReplyManager.error =  getString(R.string.required)
                    return@setOnClickListener
                }

                reportsViewModel.answerReport(reportId,answer)
            }
        }
    }
    private fun observer (){
        reportsViewModel.showReportLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelShowReport
                    binding.apply {
                        data.data.apply {

                            textReportName.text =report_name
                            textDate.text = created_at
                            textDetails.text = description
                            textUserName.text = user.first_name
                            textDate2.text = created_at
                            textDetailsManager.text = answer
                            textManagerName.text = manger.first_name+" "+manger.last_name


                            if (answer.isNotEmpty()){
                                textDate2.visibilityVisible()
                                cardView2.visibilityVisible()
                                textType2.visibilityVisible()
                                textDetailsManager.visibilityVisible()
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

        reportsViewModel.endReportLiveData.observe(this ,{
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

        reportsViewModel.answerReportLiveData.observe(this ,{
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