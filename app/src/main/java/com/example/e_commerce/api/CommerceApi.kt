package com.example.e_commerce.api

import com.example.e_commerce.model.identity.LoginBody
import com.example.e_commerce.model.identity.LoginModel
import com.example.e_commerce.model.identity.RegisterBody
import com.example.e_commerce.model.identity.UserInfoModel
import com.example.e_commerce.model.product.Product
import com.example.e_commerce.model.product.ProductModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface CommerceApi {

    @GET("/Common/Product/GetProductsForSell?page=0")

    //-------------GET PRODUCTS----------------------------------

    suspend fun getProducts(@Header("Authorization") token: String): Response<ProductModel>



    //--------------FAVORITES----------------------------------

    @GET("/Ecommerce/User/GetFavorites")

    suspend fun getFavoriteProducts(@Header("Authorization") token: String): Response<List<Product>>

    @POST("/Ecommerce/User/AddProductToFavorites")
    suspend fun addFavoriteProduct(
        @Header("Authorization") token: String,
        @Query("productId") productId: Int
    //responseBody when I don't want to get the response I only want to get the status of the response if it's
    // successful or not

    ): Response<ResponseBody>

    @POST("/Ecommerce/User/RemoveProductFromFavorties")
    suspend fun removeFavoriteProduct(
        @Header("Authorization") token: String,
        @Query("productId") productId: Int
    ): Response<ResponseBody>

    //--------------IDENTITY----------------------------------

    @POST("/Common/Identity/Register")

    suspend fun userRegister(@Body registerBody: RegisterBody): Response<ResponseBody>

    @POST("/Common/Identity/Login")
    // WE NEED A RESPONSE FOR THE TOKEN
    suspend fun userLogin(@Body loginBody:LoginBody) : Response<LoginModel>

    @GET("Common/Identity/GetUserData")
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<UserInfoModel>

    //uploading anything in the api
    //part for uploading
    @Multipart
    @POST("/Common/Identity/UploadImage")
    suspend fun uploadUserImage(@Header("Authorization") token: String,
    @Part file: MultipartBody.Part): Response<ResponseBody>

}