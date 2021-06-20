package com.magdy.hospitalsystem.data

data class ModelTaskDetails(
    val `data`: TaskDetails,
    val message: String,
    val status: Int
)

data class TaskDetails(
    val created_at: String,
    val description: String,
    val id: Int,
    val status: String,
    val task_name: String,
    val to_do: List<ToDo>,
    val user: User
)

data class ToDo(
    val created_at: String,
    val id: Int,
    val task_id: Int,
    val title: String,
    val updated_at: String
)

data class User(
    val address: String,
    val birthday: String,
    val email: String,
    val first_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val mobile: String,
    val specialist: String
)