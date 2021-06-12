package com.magdy.hospitalsystem.network

import com.magdy.hospitalsystem.data.*
import retrofit2.http.*

interface RetrofitService {

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser (@Field ("email") email :String
                              ,@Field("password") password : String
                              ,@Field("first_name") fName : String
                              ,@Field("last_name") lName : String
                              ,@Field("gender") gender : String
                              ,@Field("specialist") specialist : String
                              ,@Field("birthday") birthday : String
                              ,@Field("status") status : String
                              ,@Field("address") address : String
                              ,@Field("mobile") mobile :String
                              ,@Field("type") type  :String) : ModelUser

    @FormUrlEncoded
    @POST("login")
    suspend fun login (@Field("email") email: String
                       ,@Field("password") password: String
                       ,@Field("device_token") deviceToken :String ) : ModelUser

    @GET("doctors")
    suspend fun getAllUsers (@Query("type") type : String
                             ,@Query("name") name :String ) : ModelAllUser
    @FormUrlEncoded
    @POST("calls")
    suspend fun createCallReception (@Field("patient_name") name :String
                                     ,@Field("age") age : String
                                     ,@Field("doctor_id") doctorId : Int
                                     ,@Field("phone") phone :String
                                     ,@Field("description") description :String) : ModelCreation
    @GET("calls/{id}")
    suspend fun showCall (@Path("id") id : Int) : ModelShowCall

    @PUT("calls/{id}")
    suspend fun logoutCall (@Path ("id") id : Int) : ModelCreation

    @PUT("calls-accept/{id}")
    suspend fun acceptRejectCall (@Query("status") status :String) : ModelCreation

    @FormUrlEncoded
    @POST("show-profile")
    suspend fun showProfile (@Field("user_id") userId : Int) : ModelUser

    @GET("calls")
    suspend fun getAllCalls (@Query("date") date : String) : ModelAllCalls
}