package com.example.githubuserapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MainModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()

    fun setSeacrhUsers(query : String){
        RetrofitClient
            .apiInstance
            .getSearchUser(query)
            .enqueue(object : Callback<UserRespond>{
                override fun onResponse(call: Call<UserRespond>, response: Response<UserRespond>) {
                    listUser.postValue(response.body()?.items)
                }

                override fun onFailure(call: Call<UserRespond>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }
            })
    }

    fun getSearchUser(): LiveData<ArrayList<User>>{
        return listUser
    }
}