package com.magdy.hospitalsystem.network


import com.magdy.hospitalsystem.data.*
import com.magdy.hospitalsystem.network.RetrofitService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.http.*
import javax.inject.Inject

class RetrofitClient @Inject
constructor(
    private val retrofitService: RetrofitService,
) {

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


    suspend fun acceptRejectCall ( status :String) = withContext(IO){
        retrofitService.acceptRejectCall(status)
    }


    suspend fun showProfile ( userId : Int) = withContext(IO){
        retrofitService.showProfile(userId)
    }


    suspend fun getAllCalls (date : String) = withContext(IO){
        retrofitService.getAllCalls(date)
    }



}