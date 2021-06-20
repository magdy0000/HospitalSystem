package com.magdy.hospitalsystem.data

data class ModelAllReports(
    val `data`: List<ReportsData>,
    val message: String,
    val status: Int
)

data class ReportsData(
    val created_at: String,
    val id: Int,
    val report_name: String,
    val status: String
)