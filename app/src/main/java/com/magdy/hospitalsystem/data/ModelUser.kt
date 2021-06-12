package com.magdy.hospitalsystem.data

data class ModelUser(
    val `data`: UserData,
    val message: String,
    val status: Int
)

data class UserData(
    val access_token: String,
    val address: String,
    val birthday: String,
    val created_at: String,
    val email: String,
    val first_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val mobile: String,
    val specialist: String,
    val token_type: String,
    val type: String,
    val verified: Boolean,
    val status :String ?= null
)