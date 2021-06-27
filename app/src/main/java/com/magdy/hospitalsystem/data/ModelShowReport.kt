package com.magdy.hospitalsystem.data

data class ModelShowReport(
    val `data`: ReportData,
    val message: String,
    val status: Int
)

data class ReportData(
    val answer: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val manger: Manger,
    val note: String,
    val report_name: String,
    val status: String,
    val user: UserReport
)

data class Manger(
    val first_name: String,
    val id: String,
    val last_name: String,
    val specialist: String,
    val updated_at: String
)

data class UserReport(
    val first_name: String,
    val id: Int,
    val last_name: String,
    val specialist: String
)