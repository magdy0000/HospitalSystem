package com.magdy.hospitalsystem.ui.reception

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.databinding.FragmentReceptionHomeBinding
import com.magdy.hospitalsystem.databinding.LoginFragmentBinding
import com.magdy.hospitalsystem.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceptionHomeFragment : BaseFragment() {


    private var _binding  : FragmentReceptionHomeBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reception_home , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReceptionHomeBinding.bind(view)

        initView()
        onClicks()

    }

    private fun initView(){
        binding.apply {
            imageProfile.setImageResource(getImage())
            textName.text = MySharedPreferences.getUserName()
            textType.text=  MySharedPreferences.getUserType()
        }
    }
    private fun onClicks (){

        binding.apply {
            cardCalls.setOnClickListener {
                navigate(ReceptionHomeFragmentDirections.actionReceptionHomeFragmentToReceptionCallsFragment())
            }
            cardAttendance.setOnClickListener {

                navigate(ReceptionHomeFragmentDirections.actionReceptionHomeFragmentToAttendanceFragment())
            }

           cardProfile.setOnClickListener {
                navigate(ReceptionHomeFragmentDirections
                    .actionReceptionHomeFragmentToProfileFragment(true , MySharedPreferences.getUserId()))
            }

            cardTasks.setOnClickListener {
                navigate(ReceptionHomeFragmentDirections.actionReceptionHomeFragmentToTasksFragment())
            }

            cardReports.setOnClickListener {

                navigate(ReceptionHomeFragmentDirections.actionReceptionHomeFragmentToReportsFragment())
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}