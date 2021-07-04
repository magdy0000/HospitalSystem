package com.magdy.hospitalsystem.ui.common.calls_cases

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
class GeneralCallsAndCasesViewModel
@Inject constructor(private val retrofitClient: RetrofitClient) : ViewModel(){
    private val _getCallsLiveData = SingleLiveEvent<NetworkState>()
    val getCallsLiveData get() = _getCallsLiveData

    fun getCalls() {
        _getCallsLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.getAllCalls("")
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


    private val _acceptRejectCallLiveData = SingleLiveEvent<NetworkState>()
    val acceptRejectCallLiveData get() = _acceptRejectCallLiveData

    fun acceptRejectCall(id : Int,status : String) {
        _acceptRejectCallLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.acceptRejectCall(id,status)
                if (data.status == 1) {
                    _acceptRejectCallLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _acceptRejectCallLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _acceptRejectCallLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

    private val _showMedicalRecordDoctorLiveData = SingleLiveEvent<NetworkState>()
    val showMedicalRecordDoctorLiveData get() = _showMedicalRecordDoctorLiveData

    fun showMedicalRecordDoctor(id : Int) {
        _showMedicalRecordDoctorLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.showMedicalRecordDoctor(id)
                if (data.status == 1) {
                    _showMedicalRecordDoctorLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _showMedicalRecordDoctorLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _showMedicalRecordDoctorLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }



    private val _allCasesLiveData = SingleLiveEvent<NetworkState>()
    val allCasesLiveData get() = _allCasesLiveData

    fun getAllCases() {
        _allCasesLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.getAllCases()
                if (data.status == 1) {
                    _allCasesLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _allCasesLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _allCasesLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

    private val _uploadMeasurementLiveData = SingleLiveEvent<NetworkState>()
    val uploadMeasurementLiveData get() = _uploadMeasurementLiveData

    fun uploadMeasurement(caseId : Int
                          ,bloodPressure :String
                          , sugarAnalysis :String
                          , tempreture :String
                          ,fluidBalance :String
                          , respiratoryRate :String
                          , heartRate :String
                          , note :String) {
        _uploadMeasurementLiveData.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.uploadMeasurement(caseId,bloodPressure , sugarAnalysis
                    ,tempreture,fluidBalance ,respiratoryRate,heartRate, note)
                if (data.status == 1) {
                    _uploadMeasurementLiveData.postValue(NetworkState.getLoaded(data))
                } else {
                    _uploadMeasurementLiveData.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _uploadMeasurementLiveData.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }



}