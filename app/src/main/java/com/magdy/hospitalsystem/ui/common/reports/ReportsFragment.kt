package com.magdy.hospitalsystem.ui.common.reports

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerAllReports
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.CallsData
import com.magdy.hospitalsystem.data.ModelAllCalls
import com.magdy.hospitalsystem.data.ModelAllReports
import com.magdy.hospitalsystem.data.ReportsData
import com.magdy.hospitalsystem.databinding.FragmentReceptionHomeBinding
import com.magdy.hospitalsystem.databinding.FragmentReportsBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.ui.reception.ReceptionViewModel
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ReportsFragment : BaseFragment() {

    private var _binding  : FragmentReportsBinding?= null
    private val binding get() = _binding!!
    private val adapterRecyclerReports : AdapterRecyclerAllReports
                         by lazy { AdapterRecyclerAllReports() }
    private val cal: Calendar = Calendar.getInstance()
    private var startDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val reportsViewModel : ReportsViewModel by viewModels()
    private val fullFormat = SimpleDateFormat("yyyy-MM-dd")

    private var date = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reports , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReportsBinding.bind(view)

        reportsViewModel.getAllReports(date)
        onClicks()
        observers()
    }

    private fun observers(){
        reportsViewModel.getAllReportsLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelAllReports
                    adapterRecyclerReports.list = data.data as ArrayList<ReportsData>
                    binding.recyclerCalls.adapter = adapterRecyclerReports
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
        binding.textDate.text = fullFormat.format(cal.time)

        adapterRecyclerReports.onReportClick = object: AdapterRecyclerAllReports.OnReportClick{
            override fun onClick(id: Int) {
                navigate(ReportsFragmentDirections.actionReportsFragmentToShowReportFragment(id))
            }
        }

        binding.apply {
            btnDate.setOnClickListener {
                dataPicker()
            }
            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }
            btnAddReport.setOnClickListener {

                navigate(ReportsFragmentDirections.actionReportsFragmentToCreateReportFragment())
            }
        }

        startDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                val realMonth = month +1

                val  myMonth =  if (realMonth < 10) "0$realMonth" else realMonth.toString()
                val myday = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()

                date = "${year}-${myMonth}-${myday}"
                binding.textDate.text = date
                reportsViewModel.getAllReports(date)
            }
    }

    private fun dataPicker() {
        val dialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            startDateSetListener,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}