package com.magdy.hospitalsystem.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.network.RetrofitClient
import com.magdy.hospitalsystem.network.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel
@Inject constructor(private val retrofitClient: RetrofitClient) : ViewModel(){


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


    private val _showMedicalRecordAnalysis = SingleLiveEvent<NetworkState>()
    val showMedicalRecordAnalysis get() = _showMedicalRecordAnalysis

    fun showMedicalRecordAnalysis(id : Int) {
        _showMedicalRecordAnalysis.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.showMedicalRecordAnalysis(id)
                if (data.status == 1) {
                    _showMedicalRecordAnalysis.postValue(NetworkState.getLoaded(data))
                } else {
                    _showMedicalRecordAnalysis.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _showMedicalRecordAnalysis.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

    private val _uploadRecordAnalysis = SingleLiveEvent<NetworkState>()
    val uploadRecordAnalysis get() = _uploadRecordAnalysis

    fun uploadRecordAnalysis(part: MultipartBody.Part
                             , call_id: RequestBody
                             , status: RequestBody
                             , note: RequestBody) {
        _uploadRecordAnalysis.postValue(NetworkState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = retrofitClient.uploadMedicalRecord(part, call_id , status , note)
                if (data.status == 1) {
                    _uploadRecordAnalysis.postValue(NetworkState.getLoaded(data))
                } else {
                    _uploadRecordAnalysis.postValue(NetworkState.getErrorMessage(data.message))

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _uploadRecordAnalysis.postValue(NetworkState.getErrorMessage(ex))
            }
        }
    }

}