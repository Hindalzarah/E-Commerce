package com.example.e_commerce.model.identity


import com.google.gson.annotations.SerializedName

data class UserInfoModel(
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("image")
    val image: String
)