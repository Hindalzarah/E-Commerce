package com.example.e_commerce.view.identity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.identity.LoginBody
import com.example.e_commerce.model.identity.LoginModel
import com.example.e_commerce.model.product.Product
import com.example.e_commerce.repository.ApiServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "LoginViewModel"
class LoginViewModel: ViewModel() {

    val apiRepo = ApiServiceRepository.get()

    val loginLiveData = MutableLiveData<LoginModel>()
    val loginErrorLiveData = MutableLiveData<String>()



    fun login(email: String, password: String){

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = apiRepo.login(LoginBody(email,password,true))
                if(response.isSuccessful){
                    Log.d(TAG,response.body().toString())
                    response.body()?.run {
                        loginLiveData.postValue(this)
                    }
                } else {
                    Log.d(TAG,response.message())
                    loginErrorLiveData.postValue(response.message())
                }


            } catch(e:Exception) {


                Log.d(TAG, e.message.toString())
                loginErrorLiveData.postValue(e.message.toString())
            }

        }

    }
}