package com.example.e_commerce.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.e_commerce.api.CommerceApi
import com.example.e_commerce.model.identity.LoginBody
import com.example.e_commerce.model.identity.RegisterBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception

 const val SHARED_PREF_FILE = "Auth"
const val TOKEN_KEY = "TOKEN"
private const val BASE_URL  = "http://18.196.156.64"
class ApiServiceRepository(val context: Context) {

    private val retrofitService = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

    private val retrofitApi = retrofitService.create(CommerceApi::class.java)


    //design pattern

    //----------------------
    //shared preference

    //USED THE CONTEXT CAUSE WE'RE NOT INSIDE THE ACTIVITY SO WE HAVE TO USE IT TO GET THE SHARED PREFERENCE
    private val sharedPref = context.getSharedPreferences(SHARED_PREF_FILE,Context.MODE_PRIVATE)

    //----------------get products--------------
        //calling file
// val accessToken = sharedPref.getString(TOKEN_KEY,"")
    //calling token from the file
    suspend fun getProducts() = retrofitApi.getProducts("Bearer ${sharedPref.getString(TOKEN_KEY,"")}")


    suspend fun addFavoriteProduct(productId: Int) =  retrofitApi.addFavoriteProduct("Bearer ${sharedPref.getString(TOKEN_KEY,"")}", productId)
    suspend fun removeFavoriteProduct(productId: Int) =  retrofitApi.removeFavoriteProduct("Bearer ${sharedPref.getString(TOKEN_KEY,"")}", productId)
    suspend fun register(registerBody: RegisterBody) = retrofitApi.userRegister(registerBody)
    suspend fun login(loginBody: LoginBody) = retrofitApi.userLogin(loginBody)
    suspend fun getUserInfo() = retrofitApi.getUserInfo("Bearer ${sharedPref.getString(TOKEN_KEY,"")}")
    suspend fun uploadImage(file: File): Response<ResponseBody>  {

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("imageFile",file.name,requestFile)

        return retrofitApi.uploadUserImage("Bearer ${sharedPref.getString(TOKEN_KEY,"")}",body)
    }



    companion object{

        private var instance: ApiServiceRepository? = null

        fun init(context: Context){
            if(instance == null)
                instance = ApiServiceRepository(context)

        }
        fun get(): ApiServiceRepository {

            return instance ?: throw  Exception("ApiServiceRepository Must Be Initialized")
        }
    }
}