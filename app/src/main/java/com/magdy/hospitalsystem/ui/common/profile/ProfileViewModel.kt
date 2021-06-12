package com.magdy.hospitalsystem.ui.common.profile

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
class ProfileViewModel
@Inject constructor(private val retrofitClient: RetrofitClient ) : ViewModel() {


    private val _showProfileLiveData = SingleLiveEvent<NetworkState>()
    val showProfileLiveData get() = _showProfileLiveData

    fun getProfile(id: Int) {
        _showProfileLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.showProfile(id)
                if (data.status == 1) {
                    _showProfileLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _showProfileLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _showProfileLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

}