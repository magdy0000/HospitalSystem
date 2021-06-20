package com.magdy.hospitalsystem.data

data class ModelMedicalMeasurement(
    val `data`: Data,
    val message: String,
    val status: Int
)

data class Data(
    val blood_pressure: String,
    val id: Int,
    val note: String,
    val status: String,
    val sugar_analysis: String
)