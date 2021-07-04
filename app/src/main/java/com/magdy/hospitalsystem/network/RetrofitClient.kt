package com.magdy.hospitalsystem.network


import com.magdy.hospitalsystem.data.*
import com.magdy.hospitalsystem.network.RetrofitService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import javax.inject.Inject

class RetrofitClient @Inject
constructor(
    private val retrofitService: RetrofitService) {

    suspend fun registerUser(email: String,
        password: String, fName: String, lName: String,
        gender: String, specialist: String,
        birthday: String, status: String,
        address: String, mobile: String, type: String
    ) = withContext(IO) {
        retrofitService.registerUser(
            email, password, fName,
            lName, gender, specialist, birthday,
            status, address, mobile, type
        )
    }

    suspend fun login ( email: String
                       , password: String
                       ,deviceToken :String ) = withContext(IO){
                           retrofitService.login(email,password,deviceToken)
    }
    suspend fun getAllUsers (type : String
                             , name :String )  = withContext(IO){
        retrofitService.getAllUsers(type,name)
    }

    suspend fun createCallReception ( name :String
                                     , age : String
                                     , doctorId : Int
                                     , phone :String
                                     , description :String)  = withContext(IO){
        retrofitService.createCallReception(name,age,doctorId,phone,description)
    }

    suspend fun showCall (id : Int)  = withContext(IO){
        retrofitService.showCall(id)
    }


    suspend fun logoutCall (id :Int) = withContext(IO){
        retrofitService.logoutCall(id)
    }


    suspend fun acceptRejectCall (id : Int, status :String) = withContext(IO){
        retrofitService.acceptRejectCall(id,status)
    }


    suspend fun showAllTasks ( date :String) = withContext(IO){
        retrofitService.showAllTasks(date)
    }

    suspend fun execution (id : Int
                           ,note :String) = withContext(IO){
        retrofitService.execution(id,note)
    }


    suspend fun showProfile ( userId : Int) = withContext(IO){
        retrofitService.showProfile(userId)
    }


    suspend fun getAllCalls (date : String) = withContext(IO){
        retrofitService.getAllCalls(date)
    }

    suspend fun showTask ( id : Int)  = withContext(IO){
        retrofitService.showTask(id)
    }

    suspend fun getAllReports (date  :String)  = withContext(IO){
        retrofitService.getAllReports(date)
    }

    suspend fun answerReport ( id : Int
                              ,  answer :String) = withContext(IO){
        retrofitService.answerReport(id, answer)
    }

    suspend fun endReport (id : Int) = withContext(IO){
        retrofitService.endReport(id)
    }
    suspend fun attendance (status : String ) = withContext(IO){
        retrofitService.attendance(status)
    }

    suspend fun createReport (reportName : String
                              ,description :String) = withContext(IO){
        retrofitService.createReport(reportName, description)
    }

    suspend fun getAllCases()= withContext(IO){
        retrofitService.getAllCases()
    }


    suspend fun showCase(id: Int)= withContext(IO){
        retrofitService.showCase(id)
    }


    suspend fun addNurse ( calId : Int
                          , nurseId : Int ) = withContext(IO){
        retrofitService.addNurse(calId,nurseId)
    }


    suspend fun requestAnalysis ( callId : Int
                                             ,userId : Int
                                             , note : String
                                             , types : List<String> ) = withContext(IO){
        retrofitService.requestAnalysis(callId,userId , note,  types)
    }


    suspend fun sendCallManager (userId : Int
                                 ,description :String ) = withContext(IO){
        retrofitService.sendCallManager(userId ,description)
    }




    suspend fun uploadMeasurement (caseId : Int
                                   ,bloodPressure :String
                                   , sugarAnalysis :String
                                   , tempreture :String
                                   ,fluidBalance :String
                                   , respiratoryRate :String
                                   , heartRate :String
                                   , note :String
                                    ) = withContext(IO){
        retrofitService.uploadMeasurement(caseId,bloodPressure , sugarAnalysis
            ,tempreture,fluidBalance ,respiratoryRate,heartRate, note, "done")
    }



    suspend fun sendNotification (userId  : Int
                                  ,title : String
                                  , body : String)= withContext(IO){
        retrofitService.sendNotification(userId, title ,body)
    }




    suspend fun showMedicalRecordDoctor ( caseId  : Int) = withContext(IO){
        retrofitService.showMedicalRecordDoctor(caseId)
    }


    suspend fun showMedicalRecordAnalysis ( caseId  : Int) = withContext(IO){
        retrofitService.showMedicalRecordAnalysis(caseId)
    }

    suspend fun createTasks ( userId : Int
                             , taskName  :String
                             , description :String
                             , todoList : List<String> ) = withContext(IO){
        retrofitService.createTasks(userId,taskName,description,todoList)
    }


    suspend fun uploadMedicalRecord ( part: MultipartBody.Part
                                     , call_id: RequestBody
                                     , status: RequestBody
                                     , note: RequestBody
    ) = withContext(IO){
        retrofitService.uploadMedicalRecord(part, call_id , status , note)
    }

    suspend fun showReport (id : Int ) = withContext(IO){
        retrofitService.showReport(id)
    }





}