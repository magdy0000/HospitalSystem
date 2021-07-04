package com.magdy.hospitalsystem.ui.hr

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelUser
import com.magdy.hospitalsystem.databinding.FragmentRegisterNewEmployeeBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.Const.ANALYSIS
import com.magdy.hospitalsystem.utils.Const.ANALYSIS_VIEW_KEY
import com.magdy.hospitalsystem.utils.ProgressLoading
import com.magdy.hospitalsystem.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RegisterNewEmployeeFragment  :BaseFragment() {

    private var _binding : FragmentRegisterNewEmployeeBinding?= null
    private val binding get() = _binding!!

    private val cal: Calendar = Calendar.getInstance()
    private var startDateSetListener: DatePickerDialog.OnDateSetListener? = null
    var  birthday = ""
    val hrViewModel : HrViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_new_employee , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterNewEmployeeBinding.bind(view)

        onClicks()
        observers()
    }

    private fun onClicks() {

        startDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                val realMonth = month +1

               val  myMonth =  if (realMonth < 10) "0$realMonth" else realMonth.toString()
               val myday = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()

               birthday = "${year}-${myMonth}-${myday}"
                binding.editBirthday.text = birthday
            }
       binding.apply {
           btnCreate.setOnClickListener {
               validation()
           }

           editBirthday.setOnClickListener {
               dataPicker()
           }

           btnBack.setOnClickListener {
               myActivity?.onBackPressed()
           }
       }
    }

    private fun observers (){
        hrViewModel.createUserLiveData.observe(this ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelUser

                    showToast(data.message)
                    findNavController().popBackStack()
                    ProgressLoading.dismiss()

                }
                else -> {
                    ProgressLoading.dismiss()
                    showToast(it.msg)
                }
            }
        })
    }

    private fun validation (){

        binding.apply {
            val  fName = editFname.text.toString()
            val  lName = editLname.text.toString()
            val  email = editEmail.text.toString()
            val  password = editPassword.text.toString()
            val  address = editAddress.text.toString()
            val  phone = editPhone.text.toString()
            val  gender = spinnerGender.selectedItem.toString()
            var  type = spinnerSpecialist.selectedItem.toString()
            val  status = spinnerStatus.selectedItem.toString()


            if (fName ==""){
                editFname.error = getString(R.string.required)

            }else if (lName == ""){
                editLname.error = getString(R.string.required)
            }else if (spinnerGender.selectedItemPosition == 0){

                showToast(getString(R.string.please_select_gender))
            }else if (spinnerSpecialist.selectedItemPosition == 0){
                showToast(getString(R.string.specialist_hint))
            }else if (birthday == ""){
                showToast(getString(R.string.birthday_hint))
            }else if (spinnerStatus.selectedItemPosition == 0){
                showToast(getString(R.string.status_hint))
            }else if (phone == ""){
                editPhone.error = getString(R.string.required)
            }else if (address == ""){
                editAddress.error = getString(R.string.required)
            }else if (email == ""){
                editEmail.error = getString(R.string.required)
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.editEmail.error = getString(R.string.wrong_email_address)
            }
            else if (password == ""){
                editPassword.error = getString(R.string.required)
            }else{

                if (type == ANALYSIS_VIEW_KEY){
                    type = ANALYSIS
                }
                hrViewModel.createNewUser(email
                    ,password
                    ,fName
                    ,lName
                    ,gender
                    ,type
                    ,birthday
                    ,status
                    ,address
                    ,phone
                    ,type)
            }



        }
    }

    private fun dataPicker() {
        val dialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            startDateSetListener,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}