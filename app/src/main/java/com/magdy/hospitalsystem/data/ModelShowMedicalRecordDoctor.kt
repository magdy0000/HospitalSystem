package com.magdy.hospitalsystem.data

data class ModelShowMedicalRecordDoctor(
    val `data`: MedicalRecordDoctorData,
    val message: String,
    val status: Int
)

data class MedicalRecordDoctorData(
    val id: Int,
    val image: String,
    val note: String,
    val status: String,
    val user: UserAnalysis
)

data class UserAnalysis(
    val first_name: String,
    val id: Int,
    val last_name: String,
    val specialist: String
)