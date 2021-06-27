package com.magdy.hospitalsystem.ui.nurse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.databinding.FragmentNurseHomeBinding
import com.magdy.hospitalsystem.databinding.LoginFragmentBinding
import com.magdy.hospitalsystem.ui.auth.LoginViewModel
import com.magdy.hospitalsystem.ui.doctor.DoctorHomeFragmentDirections
import com.magdy.hospitalsystem.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NurseHomeFragment : BaseFragment() {
    private var _binding  : FragmentNurseHomeBinding?= null
    private val binding get() = _binding!!

    val loginViewModel  : LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nurse_home , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNurseHomeBinding.bind(view)

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

                navigate(NurseHomeFragmentDirections.actionNurseHomeFragmentToAttendanceFragment())
            }

            cardProfile.setOnClickListener {
                navigate(
                    NurseHomeFragmentDirections
                        .actionNurseHomeFragmentToProfileFragment(
                            true,
                            MySharedPreferences.getUserId()
                        )
                )
            }
            cardCases.setOnClickListener {
                navigate(NurseHomeFragmentDirections.actionNurseHomeFragmentToDoctorAllCasesFragment())
            }

            cardTasks.setOnClickListener {
                navigate(NurseHomeFragmentDirections.actionNurseHomeFragmentToTasksFragment())
            }

            cardReports.setOnClickListener {

                navigate(NurseHomeFragmentDirections.actionNurseHomeFragmentToReportsFragment())
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}