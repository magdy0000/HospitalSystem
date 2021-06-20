package com.magdy.hospitalsystem.ui.reception

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.databinding.FragmentRequestCompleteBinding

class RequestCompleteFragment : BaseFragment() {

    private var _binding  : FragmentRequestCompleteBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_request_complete , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRequestCompleteBinding.bind(view)

        onClicks()
    }

    private fun onClicks (){
        binding.btnBackToHome.setOnClickListener {
          navigate(RequestCompleteFragmentDirections.actionRequestCompleteFragmentToReceptionHomeFragment())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}