package com.example.e_commerce.view.identity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.identity.RegisterBody
import com.example.e_commerce.repository.ApiServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

//1
private const val TAG = "RegisterViewModel"
class RegisterViewModel: ViewModel() {

    //2
    private val apiRepo = ApiServiceRepository.get()

    //3
    val registerLiveData = MutableLiveData<String>()
    val registerErrorLiveData =MutableLiveData<String>()


    fun register(firstName: String, lastName: String, email: String, password: String) {

      //4
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    apiRepo.register(RegisterBody(email, firstName, lastName, password, 1))

                if (response.isSuccessful){
                    Log.d(TAG,response.body().toString())
                    registerLiveData.postValue("Registered")
                } else {
                    Log.d(TAG,response.message())
                    registerErrorLiveData.postValue(response.message())
                }
            } catch (e: Exception) {
                Log.d(TAG,e.message.toString())
                registerErrorLiveData.postValue(e.message.toString())
            }
        }
    }
}