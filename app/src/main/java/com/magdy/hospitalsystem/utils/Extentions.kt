package com.magdy.hospitalsystem.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

import com.google.android.material.snackbar.Snackbar

fun Fragment.showToast (massage : Any?){
    Toast.makeText(requireContext() ,"$massage" , Toast.LENGTH_LONG ).show()
}

fun Fragment.showToastShort(massage : Any){
    Toast.makeText(requireContext() ,"$massage" , Toast.LENGTH_SHORT ).show()
}
fun View.visibilityInVisible (){
    this.visibility = View.INVISIBLE
}
fun View.visibilityGone (){
    this.visibility = View.GONE
}
fun View.visibilityVisible (){
    this.visibility = View.VISIBLE
}


fun Fragment.showSnackBarMessage(message : String) {
    val snack = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
    snack.show()
}
//fun Fragment.showProgress (){
//    val progress = view?.rootView?.findViewById<LoadingView>(R.id.load)
//    val fragmentContainer = view?.rootView?.findViewById<FragmentContainerView>(R.id.nav_host_fragment2)
//    progress?.start()
//    progress?.visibilityVisible()
//    fragmentContainer?.visibilityGone()
//}
//fun Fragment.hideProgress (){
//    val progress = view?.rootView?.findViewById<LoadingView>(R.id.load)
//    val fragmentContainer = view?.rootView?.findViewById<FragmentContainerView>(R.id.nav_host_fragment2)
//    progress?.stop()
//    progress?.visibilityGone()
//    fragmentContainer?.visibilityVisible()
//}
fun Fragment.hideKeyboard() {
    val inputManager: InputMethodManager = activity
        ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    // check if no view has focus:
    val currentFocusedView = activity?.currentFocus
    if (currentFocusedView != null) {
        inputManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun DialogFragment.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}