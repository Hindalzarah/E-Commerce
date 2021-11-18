package com.example.e_commerce.view.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.product.Product
import com.example.e_commerce.repository.ApiServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "ProductsViewModel"
class ProductsViewModel: ViewModel() {

    private val apiRepo = ApiServiceRepository.get()

    // live data will handle the products
    // we get the data from livedata
    val productsLiveData = MutableLiveData<List<Product>>()
    val productsErrorLiveData = MutableLiveData<String>()

    // fun calls the request
    fun callProducts(){

        viewModelScope.launch (Dispatchers.IO){
            try {
                val response = apiRepo.getProducts()

                if (response.isSuccessful) {
                    response.body()?.run {
                        Log.d(TAG,this.toString())
                        //giving the livedata the products
                        productsLiveData.postValue(products)
                    }
                } else {
                    Log.d(TAG,response.message())
                    productsErrorLiveData.postValue(response.message())
                }
            } catch (e: Exception){
                Log.d(TAG,e.message.toString())
                productsErrorLiveData.postValue(e.message.toString())
            }
        }
    }

    fun addFavoriteProduct(productId:Int) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
               val response = apiRepo.addFavoriteProduct(productId)
                if(!response.isSuccessful){
                    Log.d(TAG,response.message())
                    // live data so if we got an error make the user get the login screen
                    productsErrorLiveData.postValue(response.message())
                }
            } catch (e: Exception) {
                Log.d(TAG,e.message.toString())
                productsErrorLiveData.postValue(e.message.toString())
            }
        }
    }

    fun removeFavoriteProduct(productId:Int) {

        viewModelScope.launch (Dispatchers.IO){
            try{
              val response = apiRepo.removeFavoriteProduct(productId)

              if(!response.isSuccessful)  {
                  Log.d(TAG,response.message())
                  productsErrorLiveData.postValue(response.message())
              }
            } catch (e: Exception) {
                Log.d(TAG,e.message.toString())
                productsErrorLiveData.postValue(e.message.toString())
            }
        }
    }
}