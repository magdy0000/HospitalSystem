package com.magdy.hospitalsystem.ui.common.calls_cases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.adapters.AdapterRecyclerAllCases
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.CasesData
import com.magdy.hospitalsystem.data.ModelAllCases
import com.magdy.hospitalsystem.databinding.FragmentAllCasesDoctorBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.ui.doctor.DoctorViewModel
import com.magdy.hospitalsystem.utils.Const
import com.magdy.hospitalsystem.utils.MySharedPreferences
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCasesFragment : BaseFragment() {
    private var _binding  : FragmentAllCasesDoctorBinding?= null
    private val binding get() = _binding!!

    private val adapterRecyclerAllCases  : AdapterRecyclerAllCases
                               by lazy { AdapterRecyclerAllCases() }
    private val viewModel  : GeneralCallsAndCasesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_cases_doctor , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllCasesDoctorBinding.bind(view)

        viewModel.getAllCases()
        onClicks()
        observers()
    }

    private fun observers() {
        viewModel.allCasesLiveData.observe(this ,{

            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelAllCases

                    adapterRecyclerAllCases.list  = data.data as ArrayList<CasesData>
                    binding.recyclerDoctorCases.adapter =adapterRecyclerAllCases

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
        binding.btnBack.setOnClickListener {
            myActivity?.onBackPressed()
        }

        adapterRecyclerAllCases.onShowClick = object : AdapterRecyclerAllCases.OnShowClick{
            override fun show(id: Int) {

                if (MySharedPreferences.getUserType() == Const.DOCTOR) {
                    navigate(
                        AllCasesFragmentDirections.actionDoctorAllCasesFragmentToCaseDetailsFragment2(
                            id
                        )
                    )
                }else if (MySharedPreferences.getUserType() == Const.NURSE){
                    navigate(AllCasesFragmentDirections
                        .actionDoctorAllCasesFragmentToNurseCaseDetailsFragment(id))
                }else if (MySharedPreferences.getUserType() == Const.ANALYSIS){
                    navigate(AllCasesFragmentDirections
                        .actionDoctorAllCasesFragmentToAnalysisCaseDetailsFragment(id))
                }
            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}