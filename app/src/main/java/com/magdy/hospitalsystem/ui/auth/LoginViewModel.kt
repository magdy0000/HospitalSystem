package com.magdy.hospitalsystem.ui.auth

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
class LoginViewModel
@Inject constructor(private val retrofitClient: RetrofitClient) : ViewModel() {


    private val _loginLiveData = SingleLiveEvent<NetworkState>()
    val loginLiveData get() = _loginLiveData

    fun login(
        email: String,
        password: String,
        deviceToken: String
    ) {
        _loginLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.login(
                    email,
                    password,
                    deviceToken)
                if (data.status == 1) {
                    _loginLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _loginLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _loginLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

}