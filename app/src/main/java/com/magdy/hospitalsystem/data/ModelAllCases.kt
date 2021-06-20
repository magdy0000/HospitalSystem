package com.magdy.hospitalsystem.data

data class ModelAllCases(
    val `data`: List<CasesData>,
    val message: String,
    val status: Int
)

data class CasesData(
    val created_at: String,
    val id: Int,
    val patient_name: String
)