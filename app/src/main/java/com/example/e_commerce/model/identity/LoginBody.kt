package com.example.e_commerce.model.identity


import com.google.gson.annotations.SerializedName

data class LoginBody(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("rememberMe")
    val rememberMe: Boolean
)