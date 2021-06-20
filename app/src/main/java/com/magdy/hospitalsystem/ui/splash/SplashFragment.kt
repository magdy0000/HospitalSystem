package com.magdy.hospitalsystem.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.databinding.FragmentSplashBinding
import com.magdy.hospitalsystem.ui.auth.LoginFragmentDirections
import com.magdy.hospitalsystem.utils.Const
import com.magdy.hospitalsystem.utils.MySharedPreferences

class SplashFragment : BaseFragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSplashBinding.bind(view)
        startApp()

    }

    private fun startApp() {
        Handler(Looper.getMainLooper()).postDelayed({



            if (MySharedPreferences.getUserType() == Const.HR) {

                navigate(SplashFragmentDirections.actionSplashFragmentToHrHomeFragment())
            } else if (MySharedPreferences.getUserType() == Const.RECEPTIONIST) {

                navigate(SplashFragmentDirections.actionSplashFragmentToReceptionHomeFragment())

            } else if (MySharedPreferences.getUserType() == Const.DOCTOR) {

                navigate(SplashFragmentDirections.actionSplashFragmentToDoctorHomeFragment())

            }
            else if (MySharedPreferences.getUserType() == Const.NURSE) {

                navigate(SplashFragmentDirections.actionSplashFragmentToNurseHomeFragment())

            }
            else if (MySharedPreferences.getUserType() == Const.ANALYSIS) {

                navigate(SplashFragmentDirections.actionSplashFragmentToAnalysisHomeFragment())

            }
            else {
                navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            }

        }, 3000)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}