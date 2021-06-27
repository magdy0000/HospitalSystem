package com.magdy.hospitalsystem.network

import com.magdy.hospitalsystem.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RetrofitService {

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("first_name") fName: String,
        @Field("last_name") lName: String,
        @Field("gender") gender: String,
        @Field("specialist") specialist: String,
        @Field("birthday") birthday: String,
        @Field("status") status: String,
        @Field("address") address: String,
        @Field("mobile") mobile: String,
        @Field("type") type: String
    ): ModelUser


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_token") deviceToken: String
    ): ModelUser

    @GET("doctors")
    suspend fun getAllUsers(
        @Query("type") type: String, @Query("name") name: String
    ): ModelAllUser

    @FormUrlEncoded
    @POST("calls")
    suspend fun createCallReception(
        @Field("patient_name") name: String,
        @Field("age") age: String,
        @Field("doctor_id") doctorId: Int,
        @Field("phone") phone: String,
        @Field("description") description: String
    ): ModelCreation

    @GET("calls/{id}")
    suspend fun showCall(@Path("id") id: Int): ModelShowCall

    @PUT("calls/{id}")
    suspend fun logoutCall(@Path("id") id: Int): ModelCreation

    @PUT("calls-accept/{id}")
    suspend fun acceptRejectCall(
        @Path("id") id: Int,
        @Query("status") status: String
    ): ModelCreation

    @FormUrlEncoded
    @POST("show-profile")
    suspend fun showProfile(@Field("user_id") userId: Int): ModelUser

    @GET("calls")
    suspend fun getAllCalls(@Query("date") date: String): ModelAllCalls

    @FormUrlEncoded
    @POST("attendance")
    suspend fun attendance(@Field("status") status: String): ModelCreation

    @GET("tasks")
    suspend fun showAllTasks(@Query("date") date: String): ModelAllTasks

    @FormUrlEncoded
    @POST("tasks")
    suspend fun createTasks (@Field("user_id") userId : Int
                             ,@Field("task_name") taskName  :String
                             ,@Field("description") description :String
                             ,@Field("todos[]") todoList : List<String> ) : ModelCreation
    @FormUrlEncoded
    @PUT("tasks/{id}")
    suspend fun execution(
        @Path("id") id: Int, @Field("note") note: String
    ): ModelCreation

    @GET("tasks/{id}")
    suspend fun showTask(@Path("id") id: Int): ModelTaskDetails

    @GET("reports")
    suspend fun getAllReports(@Query("date") date: String): ModelAllReports

    @DELETE("reports/{id}")
    suspend fun endReport(@Path("id") id: Int): ModelCreation

    @GET("reports/{id}")
    suspend fun showReport (@Path("id") id : Int ) : ModelShowReport

    @FormUrlEncoded
    @PUT("reports/{id}")
    suspend fun answerReport (@Path("id") id : Int
                              , @Field("answer") answer :String) : ModelCreation

    @FormUrlEncoded
    @POST("reports")
    suspend fun createReport(
        @Field("report_name") reportName: String, @Field("description") description: String
    ): ModelCreation

    @GET("case")
    suspend fun getAllCases(): ModelAllCases

    @GET("case/{id}")
    suspend fun showCase(@Path("id") id: Int): ModelCaseDetails

    @FormUrlEncoded
    @POST("add-nurse")
    suspend fun addNurse (@Field("call_id") calId : Int
                          ,@Field("user_id") nurseId : Int ) : ModelCreation

    @FormUrlEncoded
    @POST("make-request")
    suspend fun requestAnalysis (@Field("call_id") callId : Int
                                 ,@Field("user_id") userId : Int
                                 ,@Field("note") note : String
                                 ,@Field("types[]") types : List<String> ) : ModelCreation

    @FormUrlEncoded
    @POST("measurement")
    suspend fun uploadMeasurement (@Field("call_id") caseId : Int
                                   ,@Field("blood_pressure") bloodPressure :String
                                   ,@Field("sugar_analysis") sugarAnalysis :String
                                   ,@Field("note") not :String
                                   ,@Field("status") status : String ) : ModelCreation



    @FormUrlEncoded
    @POST("medical-record-show")
    suspend fun showMedicalRecordDoctor (@Field("call_id") caseId  : Int) :ModelShowMedicalRecordDoctor

    @FormUrlEncoded
    @POST("medical-record-show")
    suspend fun showMedicalRecordAnalysis (@Field("call_id") caseId  : Int) :ModelShowMedicalRecordAnalysis


    @FormUrlEncoded
    @POST("calls-manger")
    suspend fun sendCallManager (@Field("user_id") userId : Int
                                 ,@Field("description")description :String ) : ModelCreation
    @Multipart
    @POST("medical-record")
    suspend fun uploadMedicalRecord (@Part part: MultipartBody.Part
                                     ,@Part("call_id") call_id: RequestBody
                                     ,@Part("status") c: RequestBody
                                     ,@Part("note") note: RequestBody) : ModelCreation

}