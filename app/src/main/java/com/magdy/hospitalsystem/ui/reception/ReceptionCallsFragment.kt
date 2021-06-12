package com.magdy.hospitalsystem.ui.reception

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerAllCalls
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.CallsData
import com.magdy.hospitalsystem.data.ModelAllCalls
import com.magdy.hospitalsystem.data.ModelUser
import com.magdy.hospitalsystem.databinding.FragmentReceptionCallsBinding
import com.magdy.hospitalsystem.databinding.FragmentReceptionHomeBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ReceptionCallsFragment : BaseFragment(){

    private var _binding  : FragmentReceptionCallsBinding?= null
    private val binding get() = _binding!!
    private val cal: Calendar = Calendar.getInstance()
    private var startDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val receptionViewModel : ReceptionViewModel by viewModels()
    private val fullFormat = SimpleDateFormat("yyyy-MM-dd")
    private val adapterRecyclerAllCalls : AdapterRecyclerAllCalls by lazy { AdapterRecyclerAllCalls() }

    var date = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reception_calls , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReceptionCallsBinding.bind(view)

        initView()
        onClicks()
        observer()

    }
    private fun initView (){
        date = fullFormat.format(cal.time)
        binding.textDate.text =date

        receptionViewModel.getCalls(date)

    }
    private fun observer (){


        receptionViewModel.getCallsLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelAllCalls
                    adapterRecyclerAllCalls.list = data.data as ArrayList<CallsData>
                    binding.recyclerCalls.adapter = adapterRecyclerAllCalls
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

        startDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                val realMonth = month +1

                val  myMonth =  if (realMonth < 10) "0$realMonth" else realMonth.toString()
                val myday = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()

                date = "${year}-${myMonth}-${myday}"
                binding.textDate.text = date
                receptionViewModel.getCalls(date)
            }
        binding.apply {
            textDate.setOnClickListener {
                dataPicker()
            }

            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }
            btnAddCall.setOnClickListener {
                navigate(ReceptionCallsFragmentDirections.actionReceptionCallsFragmentToCreateCallFragment())
            }
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