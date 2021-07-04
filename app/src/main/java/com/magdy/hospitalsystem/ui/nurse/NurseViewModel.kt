package com.magdy.hospitalsystem.ui.nurse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magdy.hospitalsystem.network.NetworkState
import com.magdy.hospitalsystem.network.RetrofitClient
import com.magdy.hospitalsystem.network.SingleLiveEvent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class NurseViewModel
@Inject constructor(private val retrofitClient: RetrofitClient) : ViewModel() {


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
    private val _showCaseLiveData = SingleLiveEvent<NetworkState>()
    val showCaseLiveData get() = _showCaseLiveData


     fun sendNotification (userId  : Int
                                  ,title : String
                                  , body : String){

         viewModelScope.launch(Dispatchers.IO) {
             retrofitClient.sendNotification(userId, title, body)
         }
    }

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


}