package com.magdy.hospitalsystem.ui.hr


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
class HrViewModel
@Inject
constructor (private val retrofitClient: RetrofitClient) : ViewModel() {


    private val _createUserLiveData = SingleLiveEvent<NetworkState>()
    val createUserLiveData get() =  _createUserLiveData

    fun createNewUser ( email: String,
                        password: String,
                        fName: String,
                        lName: String,
                        gender: String,
                        specialist: String,
                        birthday: String,
                        status: String,
                        address: String,
                        mobile: String,
                        type: String){
        _createUserLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.registerUser(email,
                    password,
                    fName,
                    lName,
                    gender,
                    specialist,
                    birthday,
                    status,
                    address,
                    mobile,
                    type)
                if (data.status == 1) {
                    _createUserLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _createUserLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _createUserLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

    private val _getAllUsersLiveData = SingleLiveEvent<NetworkState>()
    val getAllUsersLiveData get() = _getAllUsersLiveData

    fun getAllUsers(
        type: String,
        name: String,
    ) {
        _getAllUsersLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.getAllUsers(type, name)
                if (data.status == 1) {
                    _getAllUsersLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _getAllUsersLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _getAllUsersLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }
}