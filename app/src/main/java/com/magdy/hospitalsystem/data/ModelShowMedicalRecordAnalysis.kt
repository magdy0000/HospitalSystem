package com.magdy.hospitalsystem.data

data class ModelShowMedicalRecordAnalysis(
    val `data`: MedicalRecordAnalysisData,
    val message: String,
    val status: Int
)

data class MedicalRecordAnalysisData(
    val id: Int,
    val note: String,
    val status: String,
    val type: List<String>,
    val user: UserDoctor
)

data class UserDoctor(
    val first_name: String,
    val id: Int,
    val last_name: String,
    val specialist: String
)