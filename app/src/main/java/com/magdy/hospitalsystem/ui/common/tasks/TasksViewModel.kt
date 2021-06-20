package com.magdy.hospitalsystem.ui.common.tasks

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
class TasksViewModel
@Inject constructor(private val retrofitClient: RetrofitClient)  : ViewModel() {


    private val _showAllTasksLiveData = SingleLiveEvent<NetworkState>()
    val showAllTasksLiveData get() = _showAllTasksLiveData

    fun showAllTasks(date : String) {
        _showAllTasksLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.showAllTasks(date)
                if (data.status == 1) {
                    _showAllTasksLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _showAllTasksLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _showAllTasksLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }


    private val _executionLiveData = SingleLiveEvent<NetworkState>()
    val executionLiveData get() = _executionLiveData

    fun execution(id : Int , note : String) {
        _executionLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.execution(id , note)
                if (data.status == 1) {
                    _executionLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _executionLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _executionLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }


    private val _showTaskLiveData = SingleLiveEvent<NetworkState>()
    val showTaskLiveData get() = _showTaskLiveData

    fun showTask(id : Int ) {
        _showTaskLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.showTask(id)
                if (data.status == 1) {
                    _showTaskLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _showTaskLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _showTaskLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

}