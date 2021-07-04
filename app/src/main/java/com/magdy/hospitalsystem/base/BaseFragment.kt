package com.magdy.hospitalsystem.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.utils.Const
import com.magdy.hospitalsystem.utils.MySharedPreferences

open class BaseFragment  : Fragment() {

    protected var myContext: Context? = null
    protected var myView: View? = null
    protected var myActivity: FragmentActivity? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        myActivity = requireActivity()
    }

    protected fun navigate(navDirections: NavDirections?) {
        Navigation.findNavController(myView!!).navigate(navDirections!!)
    }

    protected fun getImage () : Int{

        return when {
            MySharedPreferences.getUserType() == Const.DOCTOR -> {
                R.drawable.ic_doctor
            }
            MySharedPreferences.getUserType() == Const.NURSE -> {
                R.drawable.ic_nurse
            }
            MySharedPreferences.getUserType() == Const.RECEPTIONIST -> {
                R.drawable.ic_receptionist
            }
            MySharedPreferences.getUserType() == Const.MANAGER -> {
                R.drawable.ic_manager
            }
            MySharedPreferences.getUserType() == Const.HR -> {
                R.drawable.ic_hr
            }
            else -> {
                R.drawable.ic_laboratory
            }
        }

    }
}