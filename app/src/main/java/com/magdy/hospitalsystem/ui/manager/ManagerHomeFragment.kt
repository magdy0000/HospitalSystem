package com.magdy.hospitalsystem.ui.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.databinding.FragmentCaseDetailsBinding
import com.magdy.hospitalsystem.databinding.FragmentDoctorHomeBinding
import com.magdy.hospitalsystem.databinding.FragmentHomeManagerBinding
import com.magdy.hospitalsystem.ui.doctor.CaseDetailsFragmentArgs
import com.magdy.hospitalsystem.ui.doctor.DoctorViewModel
import com.magdy.hospitalsystem.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagerHomeFragment : BaseFragment() {


    private var _binding  : FragmentHomeManagerBinding?= null
    private val binding get() = _binding!!


    private val managerViewModel  : ManagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_manager , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeManagerBinding.bind(view)

        onClicks()
        observers()
    }

    private fun observers() {


    }

    private fun onClicks() {
        binding.textType.text = MySharedPreferences.getUserType()
        binding.textName.text  = MySharedPreferences.getUserName()
        binding.apply {
            cardCases.setOnClickListener {
                navigate(ManagerHomeFragmentDirections.actionManagerHomeFragmentToDoctorAllCasesFragment())
            }
            cardProfile.setOnClickListener {
                navigate(ManagerHomeFragmentDirections
                    .actionManagerHomeFragmentToProfileFragment(true ,
                    MySharedPreferences.getUserId()))
            }
            cardAttendance.setOnClickListener {
                navigate(ManagerHomeFragmentDirections.actionManagerHomeFragmentToAttendanceFragment())
            }
            cardEmployee.setOnClickListener {
                navigate(ManagerHomeFragmentDirections.actionManagerHomeFragmentToEmployeeFragment())
            }
            cardReports.setOnClickListener {
                navigate(ManagerHomeFragmentDirections.actionManagerHomeFragmentToReportsFragment())
            }

            cardTasks.setOnClickListener {
                navigate(ManagerHomeFragmentDirections.actionManagerHomeFragmentToTasksFragment())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=  null
    }


}