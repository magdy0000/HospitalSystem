package com.magdy.hospitalsystem.ui.common.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.network.RetrofitClient
import com.magdy.hospitalsystem.network.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AttendanceViewModel
@Inject constructor(private val retrofitClient: RetrofitClient) : ViewModel(){


    private val _attendanceLiveData = SingleLiveEvent<NetworkState>()
    val attendanceLiveData get() = _attendanceLiveData


    fun makeAttendance(status: String ) {
        _attendanceLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val data = retrofitClient.attendance(status)

                if (data.status == 1) {
                    _attendanceLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _attendanceLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _attendanceLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

}