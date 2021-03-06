package com.magdy.hospitalsystem.ui.hr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.databinding.FragmentHomeHrBinding
import com.magdy.hospitalsystem.ui.reception.ReceptionHomeFragmentDirections
import com.magdy.hospitalsystem.utils.MySharedPreferences
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HrHomeFragment : BaseFragment() {

   private var _binding : FragmentHomeHrBinding ?= null
    private val binding get() = _binding!!


    val hrViewModel : HrViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_hr , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeHrBinding.bind(view)


        initView()
        onClicks()
    }

    private fun initView (){
        binding.apply {
            imageProfile.setImageResource(getImage())
            textType.text = MySharedPreferences.getUserType()
            textUserName.text  = MySharedPreferences.getUserName()

            cardAttendance.setOnClickListener {

                navigate(HrHomeFragmentDirections.actionHrHomeFragmentToAttendanceFragment())
            }


            cardEmploye.setOnClickListener {
                navigate(HrHomeFragmentDirections.actionHrHomeFragmentToEmployeeFragment())
            }

            cardProfile.setOnClickListener {
                navigate(HrHomeFragmentDirections
                    .actionHrHomeFragmentToProfileFragment(true , MySharedPreferences.getUserId()))
            }

            cardTasks.setOnClickListener {
                navigate(HrHomeFragmentDirections.actionHrHomeFragmentToTasksFragment())
            }

            cardReports.setOnClickListener {
                navigate(HrHomeFragmentDirections.actionHrHomeFragmentToReportsFragment())
            }
        }
    }

    private fun onClicks(){


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}