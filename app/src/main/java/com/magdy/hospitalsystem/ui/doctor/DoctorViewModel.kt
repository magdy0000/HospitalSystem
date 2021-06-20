package com.magdy.hospitalsystem.ui.doctor

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
class DoctorViewModel
@Inject
constructor(private val retrofitClient: RetrofitClient) : ViewModel() {



    private val _showCaseLiveData = SingleLiveEvent<NetworkState>()
    val showCaseLiveData get() = _showCaseLiveData

    fun showCase(id : Int) {
        _showCaseLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.showCase(id)
                if (data.status == 1) {
                    _showCaseLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _showCaseLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _showCaseLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

    private val _addNurseLiveData = SingleLiveEvent<NetworkState>()
    val addNurseLiveData get() = _addNurseLiveData

    fun addNurse(callId : Int , nurseId  :Int) {
        _addNurseLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.addNurse(callId , nurseId)
                if (data.status == 1) {
                    _addNurseLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _addNurseLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _addNurseLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

    private val _requestAnalysisLiveData = SingleLiveEvent<NetworkState>()
    val requestAnalysisLiveData get() = _requestAnalysisLiveData

    fun requestAnalysis(callId : Int
                        ,userId : Int
                        , note : String
                        , types : List<String> ) {
        _requestAnalysisLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.requestAnalysis(callId,userId , note,  types)
                if (data.status == 1) {
                    _requestAnalysisLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _requestAnalysisLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _requestAnalysisLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

    private val _showMedicalRecordDoctor = SingleLiveEvent<NetworkState>()
    val showMedicalRecordDoctor get() = _showMedicalRecordDoctor

    fun showMedicalRecordDoctor(id : Int) {
        _showMedicalRecordDoctor.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.showMedicalRecordDoctor(id)
                if (data.status == 1) {
                    _showMedicalRecordDoctor.postValue(NetworkState.getLoaded(data))
                } else {
                    _showMedicalRecordDoctor.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _showMedicalRecordDoctor.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }


}