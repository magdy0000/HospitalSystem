package com.magdy.hospitalsystem.ui.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.databinding.FragmentAnalysisHomeBinding
import com.magdy.hospitalsystem.databinding.LoginFragmentBinding
import com.magdy.hospitalsystem.ui.auth.LoginViewModel
import com.magdy.hospitalsystem.ui.doctor.DoctorHomeFragmentDirections
import com.magdy.hospitalsystem.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AnalysisHomeFragment  : BaseFragment (){
    private var _binding  : FragmentAnalysisHomeBinding?= null
    private val binding get() = _binding!!

    val analysisViewModel  : AnalysisViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analysis_home , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAnalysisHomeBinding.bind(view)

        onClicks()
        observers()
    }

    private fun observers() {


    }

    private fun onClicks() {
        binding.textType.text = MySharedPreferences.getUserType()
        binding.textName.text  = MySharedPreferences.getUserName()
        binding.apply {



            cardAttendance.setOnClickListener {

                navigate(AnalysisHomeFragmentDirections.actionAnalysisHomeFragmentToAttendanceFragment())
            }

            cardProfile.setOnClickListener {
                navigate(
                    AnalysisHomeFragmentDirections
                        .actionAnalysisHomeFragmentToProfileFragment(
                            true,
                            MySharedPreferences.getUserId()
                        )
                )
            }
            cardCases.setOnClickListener {
                navigate(AnalysisHomeFragmentDirections.actionAnalysisHomeFragmentToDoctorAllCasesFragment())
            }

            cardTasks.setOnClickListener {
                navigate(AnalysisHomeFragmentDirections.actionAnalysisHomeFragmentToTasksFragment())
            }

            cardReports.setOnClickListener {

                navigate(AnalysisHomeFragmentDirections.actionAnalysisHomeFragmentToReportsFragment())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}