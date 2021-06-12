package com.magdy.hospitalsystem.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

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
}