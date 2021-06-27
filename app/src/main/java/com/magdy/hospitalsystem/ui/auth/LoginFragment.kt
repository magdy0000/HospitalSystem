package com.magdy.hospitalsystem.ui.auth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.data.ModelUser
import com.magdy.hospitalsystem.databinding.LoginFragmentBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.ui.splash.SplashFragmentDirections
import com.magdy.hospitalsystem.utils.Const
import com.magdy.hospitalsystem.utils.MySharedPreferences
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment :  BaseFragment() {

    private var _binding  : LoginFragmentBinding?= null
    private val binding get() = _binding!!

    val loginViewModel  : LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = LoginFragmentBinding.bind(view)

        onClicks()
        observers()
    }

    private fun onClicks (){

        binding.apply {
            btnLogin.setOnClickListener {
                validation()
            }
        }
    }

    private fun observers (){
        loginViewModel.loginLiveData.observe(this ,{

            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as  ModelUser
                    MySharedPreferences.setUserEmail(data.data.email)
                    MySharedPreferences.setUserTOKEN(data.data.access_token)
                    MySharedPreferences.setUserName(data.data.first_name + data.data.last_name)
                    MySharedPreferences.setUserPhone(data.data.mobile)
                    MySharedPreferences.setUserType(data.data.type)
                    MySharedPreferences.setUserId(data.data.id)

                    navigateUserToHome(data.data.type)

                    ProgressLoading.dismiss()

                }
                else -> {
                    ProgressLoading.dismiss()
                    showToast(it.msg)
                }
            }
        })

    }

    private  fun navigateUserToHome (type : String){

        if (type == Const.HR){
            navigate(LoginFragmentDirections.actionLoginFragmentToHrHomeFragment())
        }else if (type == Const.RECEPTIONIST){
            navigate(LoginFragmentDirections.actionLoginFragmentToReceptionHomeFragment())
        }else if (MySharedPreferences.getUserType() == Const.DOCTOR) {

            navigate(LoginFragmentDirections.actionLoginFragmentToDoctorHomeFragment())

        }else if (MySharedPreferences.getUserType() == Const.ANALYSIS) {

            navigate(LoginFragmentDirections.actionLoginFragmentToAnalysisHomeFragment())

        }
        else if (MySharedPreferences.getUserType() == Const.MANAGER) {

            navigate(LoginFragmentDirections.actionLoginFragmentToManagerHomeFragment())

        }
        else if (MySharedPreferences.getUserType() == Const.NURSE) {

            navigate(LoginFragmentDirections.actionLoginFragmentToNurseHomeFragment())

        }

    }

    private fun validation (){
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (email.isEmpty()){
            binding.editEmail.error = getString(R.string.required)

        }else if (password.isEmpty()){
            binding.editTextPassword.error = getString(R.string.required)
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.editEmail.error = getString(R.string.wrong_email_address)
        }
        else{

            FirebaseMessaging.getInstance().token
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val token = task.result
                        Log.e("TAG2", token )
                        loginViewModel.login(email, password, token)
                        return@OnCompleteListener
                    }
                })

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}