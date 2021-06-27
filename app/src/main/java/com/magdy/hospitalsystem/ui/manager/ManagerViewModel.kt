package com.magdy.hospitalsystem.ui.manager

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
class ManagerViewModel
@Inject constructor(private val retrofitClient: RetrofitClient) : ViewModel() {


    private val _createTaskLiveData = SingleLiveEvent<NetworkState>()
    val createTaskLiveData get() = _createTaskLiveData

    fun createTask(userId : Int
                   , taskName  :String
                   , description :String
                   , todoList : List<String> ) {
        _createTaskLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.createTasks(userId,taskName,description,todoList)
                if (data.status == 1) {
                    _createTaskLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _createTaskLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _createTaskLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }
}