package com.example.e_commerce.model.identity


import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("token")
    val token: String
)