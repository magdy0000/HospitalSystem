package com.magdy.hospitalsystem.ui.common.reports

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
class ReportsViewModel
@Inject constructor(private val retrofitClient: RetrofitClient): ViewModel() {




    private val _getAllReportsLiveData = SingleLiveEvent<NetworkState>()
    val getAllReportsLiveData get() = _getAllReportsLiveData

    fun getAllReports(date: String) {
        _getAllReportsLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.getAllReports(date)
                if (data.status == 1) {
                    _getAllReportsLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _getAllReportsLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _getAllReportsLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }


    private val _endReportLiveData = SingleLiveEvent<NetworkState>()
    val endReportLiveData get() = _endReportLiveData

    fun endReports(id: Int) {
        _endReportLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.endReport(id)
                if (data.status == 1) {
                    _endReportLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _endReportLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _endReportLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }
    private val _createReportLiveData = SingleLiveEvent<NetworkState>()
    val createReportLiveData get() = _createReportLiveData

    fun createReport(reportName: String, reportDescription : String) {
        _createReportLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.createReport(reportName, reportDescription)
                if (data.status == 1) {
                    _createReportLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _createReportLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _createReportLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

    private val _showReportLiveData = SingleLiveEvent<NetworkState>()
    val showReportLiveData get() = _showReportLiveData

    fun showReport(id: Int) {
        _showReportLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.showReport(id)
                if (data.status == 1) {
                    _showReportLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _showReportLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _showReportLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

    private val _answerReportLiveData = SingleLiveEvent<NetworkState>()
    val answerReportLiveData get() = _answerReportLiveData

    fun answerReport(id: Int, answer : String) {
        _answerReportLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.answerReport(id, answer)
                if (data.status == 1) {
                    _answerReportLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _answerReportLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _answerReportLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

}