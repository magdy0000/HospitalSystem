package com.magdy.hospitalsystem.ui.common.attendance

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.magdy.hospitalsystem.R
import com.magdy.hospitalsystem.base.BaseFragment
import com.magdy.hospitalsystem.data.ModelAllUser
import com.magdy.hospitalsystem.data.ModelCreation
import com.magdy.hospitalsystem.data.UsersData
import com.magdy.hospitalsystem.databinding.FragmentAttendanceBinding
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


const  val LOCATION_PERMISSION_REQUEST_CODE = 100

@AndroidEntryPoint
class AttendanceFragment  : BaseFragment() {

    private val timeFormat = SimpleDateFormat("hh:mm aa")
    private val cal = Calendar.getInstance()
    private var _binding  : FragmentAttendanceBinding?= null
    private val binding get() = _binding!!
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationCallback: LocationCallback? = null
    var userLocation : Location?= null
    private val attendanceViewModel : AttendanceViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_attendance, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAttendanceBinding.bind(view)
        initGpsSetting()
        if (isLocationPermissionAllowed()){
            locationChecker()
        }else{
            askToLocationPermission()
        }
        initView()
        observers()
        onClicks()


    }
    private fun initView (){
        binding.apply {
            textTime.text = timeFormat.format(cal.time)
            textTimeLeaving.text = timeFormat.format(cal.time)
        }

    }

    private fun observers (){

        attendanceViewModel.attendanceLiveData.observe(this@AttendanceFragment ,{
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    ProgressLoading.show(requireActivity())
                }
                NetworkState.Status.SUCCESS -> {

                    val data = it.data as ModelCreation

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

    private fun onClicks (){
        binding.apply {


                btnBack.setOnClickListener {
                    myActivity?.onBackPressed()
                }

            btnAttendance.setOnClickListener {

                if (userLocation == null){
                    binding.massage.text =getString(R.string.location_warn)
                    binding.noteUi.visibilityVisible()
                    return@setOnClickListener
                }
                if (distance(userLocation?.latitude!!
                        ,userLocation?.longitude!!
                        ,29.936074
                        , 30.883889) < 2000000){// this num for location of hospital
                            binding.noteUi.visibilityVisible()

                  //  return@setOnClickListener

                }
                binding.noteUi.visibilityGone()
                attendanceViewModel.makeAttendance(Const.ATTENDANCE)
            }
            btnLeaving.setOnClickListener {
                if (userLocation == null){
                    binding.massage.text =getString(R.string.location_warn)
                    binding.noteUi.visibilityVisible()
                    return@setOnClickListener
                }

                if (distance(userLocation?.latitude!!
                        ,userLocation?.longitude!!
                        ,30.2123213
                        ,31.132131231) < 400){
                    binding.massage.text =getString(R.string.location_warn)
                    binding.noteUi.visibilityVisible()

                //    return@setOnClickListener

                }
                binding.noteUi.visibilityGone()
                attendanceViewModel.makeAttendance(Const.LEAVING)
            }
        }


    }
    @SuppressLint("MissingPermission")
    private fun getLocation (){

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                userLocation = locationResult.lastLocation

            }
        }
        mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }

    private fun initGpsSetting (){
        if (mFusedLocationClient == null)
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (mLocationRequest == null) {
            mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1)
                .setSmallestDisplacement(5f)// update each 5 matters
        }
    }

    private fun locationChecker (){

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(requireActivity()).checkLocationSettings(builder.build())


        result.addOnCompleteListener(OnCompleteListener<LocationSettingsResponse?> { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
                getLocation()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                requireActivity(),
                                LocationRequest.PRIORITY_HIGH_ACCURACY
                            )
                        } catch (e: SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        })

    }
    fun isLocationPermissionAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askToLocationPermission() {

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LocationRequest.PRIORITY_HIGH_ACCURACY -> when (resultCode) {
                Activity.RESULT_OK ->                 // All required changes were successfully made
                   getLocation()
                Activity.RESULT_CANCELED ->                 // The user was asked to change settings, but chose not to
                    locationChecker()
                else -> {
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults[0] ==  PackageManager.PERMISSION_GRANTED){
            locationChecker()
        }


    }
    private fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
    ): Double {
        return if (lat1 == lat2 && lon1 == lon2) {
            0.0
        } else {
            val theta = lon1 - lon2
            var dist =
                sin(Math.toRadians(lat1)) * sin(
                    Math.toRadians(lat2)
                ) + cos(Math.toRadians(lat1)) * cos(
                    Math.toRadians(
                        lat2
                    )
                ) * cos(Math.toRadians(theta))
            dist = acos(dist)

            dist = Math.toDegrees(dist)
            dist *= 60 * 1.1515


            dist *= 1.609344

            dist
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}