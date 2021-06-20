package com.magdy.hospitalsystem.ui.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.databinding.FragmentDoctorHomeBinding
import com.magdy.hospitalsystem.databinding.LoginFragmentBinding
import com.magdy.hospitalsystem.ui.auth.LoginViewModel
import com.magdy.hospitalsystem.ui.reception.ReceptionHomeFragmentDirections
import com.magdy.hospitalsystem.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorHomeFragment : BaseFragment() {

    private var _binding  : FragmentDoctorHomeBinding?= null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_home , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDoctorHomeBinding.bind(view)

        onClicks()

    }

    private fun onClicks() {
        binding.textType.text = MySharedPreferences.getUserType()
        binding.textName.text  = MySharedPreferences.getUserName()
        binding.apply {


            cardCalls.setOnClickListener {
                navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToDoctorCallFragment())
            }
            cardAttendance.setOnClickListener {

                navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToAttendanceFragment())
            }

            cardProfile.setOnClickListener {
                navigate(
                    DoctorHomeFragmentDirections
                        .actionDoctorHomeFragmentToProfileFragment(
                            true,
                            MySharedPreferences.getUserId()
                        )
                )
            }
            cardCases.setOnClickListener {
                navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToDoctorAllCasesFragment())
            }

            cardTasks.setOnClickListener {
                navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToTasksFragment())
            }

            cardReports.setOnClickListener {

                navigate(DoctorHomeFragmentDirections.actionDoctorHomeFragmentToReportsFragment())
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}