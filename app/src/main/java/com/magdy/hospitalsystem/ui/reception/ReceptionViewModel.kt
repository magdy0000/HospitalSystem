package com.magdy.hospitalsystem.ui.reception

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
class ReceptionViewModel
@Inject constructor(private val retrofitClient: RetrofitClient) : ViewModel() {


    private val _getCallsLiveData = SingleLiveEvent<NetworkState>()
    val getCallsLiveData get() = _getCallsLiveData

    fun getCalls(date: String) {
        _getCallsLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.getAllCalls(date)
                if (data.status == 1) {
                    _getCallsLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _getCallsLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _getCallsLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }


    private val _createCallLiveData = SingleLiveEvent<NetworkState>()
    val createCallLiveData get() = _createCallLiveData

    fun createCall(name :String, age : String, doctorId : Int
                 , phone :String, description :String) {
        _createCallLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.createCallReception(name,age,doctorId,phone,description)
                if (data.status == 1) {
                    _createCallLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _createCallLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _createCallLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

    private val _logoutCallLiveData = SingleLiveEvent<NetworkState>()
    val logoutCallLiveData get() = _logoutCallLiveData

    fun logoutCall(id : Int) {
        _createCallLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.logoutCall(id)
                if (data.status == 1) {
                    _createCallLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _createCallLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _createCallLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }




}