package com.example.e_commerce.view.identity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.model.identity.UserInfoModel
import com.example.e_commerce.repository.ApiServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

private const val TAG = "ProfileViewModel"
class ProfileViewModel: ViewModel() {


    private val apiRepo = ApiServiceRepository.get()
    val profileLiveData = MutableLiveData<UserInfoModel>()
    val uploadImageLiveData = MutableLiveData<String>()
    val profileErrorsLiveData = MutableLiveData<String>()

   // call the request of the profile
   // give the data to the livedata
    fun callUserProfile(){
        viewModelScope.launch (Dispatchers.IO){
            try {
                val response = apiRepo.getUserInfo()
                if(response.isSuccessful) {
                    response.body()?.run{
                        profileLiveData.postValue(this)
                        Log.d(TAG,this.toString())

                    }
                } else {
                    Log.d(TAG, response.message())
                    profileErrorsLiveData.postValue(response.message().toString())
                }

            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                profileErrorsLiveData.postValue(e.message.toString())

            }
        }
    }

    fun uploadUserImage(file: File){
        viewModelScope.launch(Dispatchers.IO) {
            try {
              val response = apiRepo.uploadImage(file)

                if(response.isSuccessful) {
                    uploadImageLiveData.postValue("successful")
                } else {
                    Log.d(TAG,response.message())

                }
            } catch (e: Exception) {
                Log.d(TAG,e.message.toString())
                profileErrorsLiveData.postValue(e.message.toString())
            }
        }
    }

}