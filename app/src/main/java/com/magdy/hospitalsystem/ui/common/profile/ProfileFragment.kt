package com.magdy.hospitalsystem.ui.common.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelAllUser
import com.magdy.hospitalsystem.data.ModelUser
import com.magdy.hospitalsystem.data.UsersData
import com.magdy.hospitalsystem.databinding.FragmentProfileBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.MySharedPreferences
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import com.magdy.hospitalsystem.utils.visibilityVisible

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private var _binding  : FragmentProfileBinding?= null
    private val binding get() = _binding!!
    private val profileViewModel : ProfileViewModel by viewModels()
    private var isMine  = false
    private var userId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile , container , false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        isMine = ProfileFragmentArgs.fromBundle(requireArguments()).isMine
        userId = ProfileFragmentArgs.fromBundle(requireArguments()).id

        profileViewModel.getProfile(userId)
        onClicks()
        observer()

    }
    private fun onClicks (){
        if (isMine){
            binding.btnLogout.visibilityVisible()
        }

        binding.apply {
            btnBack.setOnClickListener {
                myActivity?.onBackPressed()
            }
            btnLogout.setOnClickListener {

                MySharedPreferences.setUserEmail("")
                MySharedPreferences.setUserTOKEN("")
                MySharedPreferences.setUserName("")
                MySharedPreferences.setUserPhone("")
                MySharedPreferences.setUserType("")
                MySharedPreferences.setUserId(0)
                navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
            }
        }


    }
    private fun observer (){

        profileViewModel.showProfileLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelUser
                    binding.apply {
                        data.data.apply {
                            textName.text = "$first_name $last_name"
                            textAddress.text = address
                            textBirthday.text = birthday
                            textEmail.text = email
                            textType.text = specialist
                            textGender.text = gender
                            textStatus.text = status.toString()
                            textPhone.text = mobile

                        }

                    }
                    ProgressLoading.dismiss()

                }
                else -> {
                    ProgressLoading.dismiss()
                    showToast(it.msg)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}